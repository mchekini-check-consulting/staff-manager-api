#!/bin/bash

cd /filebeat-7.10.1-linux-x86_64/ && ./filebeat &
java -jar /opt/staff-manager-api.jar