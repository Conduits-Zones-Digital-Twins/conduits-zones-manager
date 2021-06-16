#!/bin/bash

docker run --name=czm-mysql-database \
    -p 3306:3306 \
    -v $(pwd)/data:/var/lib/mysql \
    -e MYSQL_ROOT_PASSWORD=r00tT3st \
    -e MYSQL_DATABASE=czm-manager \
    -e MYSQL_USER=czm-manager \
    -e MYSQL_PASSWORD=czmtT3st \
    -d mysql:8.0.25