This directory contains migrate backend implementations of the webdata API, including:

- A prototype key value implementation based on mysql. To run this backend, a mysql
 instance must be present on local port 3306. Future backends will work on aws and
 google app engine.

Warning: currently, you need to have manually run the sql in migrateData.sql before
running any migrate methods.
