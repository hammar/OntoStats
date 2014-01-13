#!/bin/bash

ONTOSTATS='java -jar ontometrics-core/target/uber-ontometrics-core-0.0.1-SNAPSHOT.jar'

# remove plugins
rm plugins/uber-*

# copy all plugins to plugins
find . -name "uber-plugins-*-SNAPSHOT.jar" -exec cp '{}' plugins \; 2>/dev/null

# run the firs file and generated table headers
$ONTOSTATS -n -f $1 2>/dev/null
if [ $? -ne 0 ]; then
    echo "$1 failed to load"
fi

# now run for every other file
for item in "${@:2}"
do
    $ONTOSTATS -n -h -f $item 2>/dev/null
    if [ $? -ne 0 ]; then
        echo "$item failed to load"
    fi
done
