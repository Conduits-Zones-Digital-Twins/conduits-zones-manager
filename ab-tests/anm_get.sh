#!/bin/bash

#-n: Number of requests
#-c: Number of concurrent requests
#-H: Add header
#—r: flag to not exit on socket receive errors
#-k: Use HTTP KeepAlive feature
#-p: File containing data to POST
#-T: Content-type header to use for POST/PUT data,

#ab -n 10 -c 1 http://127.0.0.1:7070/api/czm/edge_node

#ab -n 10000 -c 10 http://192.168.1.235:7070/digitalTwin/dt00001/conf/

ab -n 10000 -c 10 http://192.168.1.89:7070/digitalTwin/dt00001/conf/