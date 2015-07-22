var WD_DATA_ID = "wd_id";
var WD_VERSION = "wd_version";
var WD_DELETED = "wd_deleted";
var WD_UPDATE_TIME = "wd_updateTime";
var WD_NAMESPACE = "wd_namespace";
var WD_CLASSNAME = "wd_classname";
var JSON_SCHEMA = "jsonSchema";
var JS_PROPERTIES = "properties";
var INDEX_LIST = "indexList";
var STATUS = "status";

// Angular lazy loading:
//     http://stackoverflow.com/questions/14855083/directive-for-lazyloading-data-in-angularjs/20415662#20415662

function buildSchema(schemaPackage, schemaName, schemaVersion, schemaProperties) {
    internalAddProperty(schemaProperties, WD_DATA_ID, "string", true); // this is a data uuid
    internalAddProperty(schemaProperties, WD_VERSION, "integer", true);
    internalAddProperty(schemaProperties, WD_DELETED, "integer", true);
    internalAddProperty(schemaProperties, WD_UPDATE_TIME, "long", true);
    internalAddProperty(schemaProperties, WD_NAMESPACE, "string", true);
    internalAddProperty(schemaProperties, WD_CLASSNAME, "string", true);

    var schemaID = schemaPackage + "." + schemaName;

    var now = new Date().getMilliseconds();

    var persistentSchema = {};
    persistentSchema[WD_VERSION] = schemaVersion;
    persistentSchema[INDEX_LIST] = null;
    persistentSchema[WD_DELETED] = 0;
    persistentSchema[STATUS] = 0;
    persistentSchema[WD_DATA_ID] = schemaID;
    persistentSchema[WD_UPDATE_TIME] = 1383073220093;
    persistentSchema[WD_NAMESPACE] = "__schema";
    persistentSchema[WD_CLASSNAME] = "com.migrate.webdata.model.PersistentSchema";

    var schemaObject = {};
    persistentSchema[JSON_SCHEMA] = schemaObject;
    schemaObject[JS_PROPERTIES] = schemaProperties;

    return JSON.stringify(persistentSchema, null, 4);
}

function internalAddProperty(schema, property, propertyType, required) {
    schema[property] = {
        "type": propertyType,
        "required": required
    };
}

angular.module('MigrateSchema.service', []).factory('MigrateService', function($http, $location) {

    return {
        postSchema: function (schemaId, schemaJson) {
            // two levels up because called from manage/schema/editor.html
            var schemaBase = "../../schema";

            var postSchemaUri = schemaBase + "/" + schemaId;

            // return the promise directly.
            var promise = $http.post(postSchemaUri, schemaJson)
            promise.then(function (result) {
                // Resolve the promise as the data
                return result.data;
            });
            promise.error(function(response, status) {
                if ( status != "" ) {
                    alert("Post schema error: " + status + ": " + response);
                }
            });
        },
        updateSchemas: function (schemaID, schemasArr) {
            // two levels up because called from dashboard/dashboard.jsp
            var schemaBase = "../schema";

            var getSchemaUri;

            if (schemaID == null) {
                getSchemaUri = schemaBase + "?syncTime=0";
            } else {
                getSchemaUri = schemaBase + "/" + schemaID;
            }

            // return the promise directly.
            return $http.get(getSchemaUri);
        }
    };
});

angular.module('MigrateSchema', ['MigrateSchema.service']);

function SchemaManagerController($scope, MigrateService) {
    $scope.schemas = [];

    $scope.updateSchemas = function(schemaID) {
        $scope.schemas = [];
        var schemas = MigrateService.updateSchemas(schemaID).
            then(function (result) {
                // resolve the promise as the data
                $scope.schemas = result.data;
            });
        $scope.schemas = schemas;
    };
}

function SchemaEditorController($scope, MigrateService) {
    $scope.migrateTypes = [
        {typeName:'String', typeValue:'string'},
        {typeName:'Integer', typeValue:'integer'},
        {typeName:'Double', typeValue:'double'},
        {typeName:'Boolean', typeValue:'boolean'}
    ];

    $scope.schemaPackage = '';
    $scope.schemaName = '';

    $scope.properties = [];
    $scope.propertyName = '';
    $scope.propertyType = null;

    $scope.deleteProperty = function(propertyName) {
        var count = 0;
        angular.forEach($scope.properties, function(property) {
            if (property.migrateProperty.name == propertyName) {
                $scope.properties.splice(count, 1);
            }
            count++;
        });
    }

    $scope.addProperty = function() {
        if (empty($scope.propertyName)) {
            alert("Missing property name.");
            return;
        }
        if (empty($scope.propertyType)) {
            alert("Select property type.");
            return;
        }

        $scope.properties.push(
            {
                migrateProperty: {
                    name: $scope.propertyName, value:{type: $scope.propertyType.typeValue, required: true}
                },
                selected: false,
                migrateType: $scope.propertyType
            });

        $scope.propertyName = '';
        $scope.propertyType = null;
    };

    function getMigrateProperties(properties) {
        var migrateProperties = {};
        angular.forEach(properties, function(property) {
            migrateProperties[property.migrateProperty.name] = property.migrateProperty.value;
        });
        return migrateProperties;
    }

    $scope.postSchema = function () {
        var schemaPackage = $scope.schemaPackage;
        var schemaName = $scope.schemaName;

        if (empty(schemaPackage)) {
            alert("Missing package name.");
            return;
        }
        if (empty(schemaName)) {
            alert("Missing schema name.");
            return;
        }
        if ($scope.properties.length == 0) {
            alert("No properties");
            return;
        }

        var schemaId = schemaPackage + "." + schemaName;
        var migrateProperties = getMigrateProperties($scope.properties);
        var schemaJson = buildSchema(schemaPackage, schemaName, 1, migrateProperties);

        MigrateService.postSchema(schemaId, schemaJson);
    };
}