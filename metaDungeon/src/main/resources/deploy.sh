#!/bin/bash
cd "$(dirname "$0")"
cd ../../../target/
scp -P 1022 metaDungeon-1*.jar arwen@immovablerod.sbs:/home/arwen/MetaDungeon-world/plugins