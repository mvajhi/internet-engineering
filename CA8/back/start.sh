#!/bin/bash

# Read MySQL password from secret file if it exists
if [ -f /run/secrets/mysql_root_password ]; then
    export MYSQL_ROOT_PASSWORD=$(cat /run/secrets/mysql_root_password)
fi

# Start the application
exec java -jar /app.jar
