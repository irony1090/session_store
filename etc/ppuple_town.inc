upstream ppuple_api {
    ip_hash;
    server localhost:9010;
    keepalive 150;
}

#upstream ppuple_front {
#    ip_hash;
#    server localhost:4000;
#    keepalive 100;
#}

#upstream ppuple_admin {
#    ip_hash;
#    server localhost:4050;
#    keepalive 100;
#}
