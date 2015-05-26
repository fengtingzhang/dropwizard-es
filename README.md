# dropwizard-es
A simple demo app using ElasticSearch Transport Client with Dropwizard

# Build
mvn package

# Run
java -jar dropwizard-es-1.0-SNAPSHOT.jar server classes/elasticsearch.yml

By default, the application is started on 
http://localhost:8080/index For indexing tweets
http://localhost:8081/ For health checks

Dependencies
ElasticSearch 1.5
https://www.elastic.co/products/elasticsearch


# Configurations
classes/elasticsearch.yml is what's used to configure the transport client
modify the host, port, and cluster name there to suit your index configuration