#!/bin/sh

#------------------------------------
# Run Load test (mute cli questions)
#------------------------------------

gatling.sh -ro reports


#------------------------------------
#For the directory of this script, set ownership of all files to same as directory
#------------------------------------
DIR=$(dirname `readlink -f $0`)
echo "Ownership dir: ${DIR}"
DIR_OWNER=$(stat -c "%u:%g" $DIR)
echo "Ownership: ${DIR_OWNER}"
set -x
chown -R $DIR_OWNER $DIR