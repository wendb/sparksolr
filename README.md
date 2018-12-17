# sparksolr
This Spark Application export all documents from old Solr collection into new Solr collection

## compile and package
$ sbt clean assembly

## Submit Spark Application
$ kinit

$ spark-submit --class solution.SolrReader --master yarn --files ./jaas-solr-client.conf --files ./ca.trustore --conf 'spark.executor.extraJavaOptions=-verbose:class -Djava.security.auth.login.config=jaas-solr-client.conf -Djavax.net.ssl.trustStore=ca.trustore -Djavax.net.ssl.trustStorePassword=password -Dsun.security.krb5.debug=true -Dsun.security.spnego.debug=true' --conf 'spark.driver.extraJavaOptions=-verbose:class -Djava.security.auth.login.config=jaas-solr-client.conf -Djavax.net.ssl.trustStore=ca.trustore -Djavax.net.ssl.trustStorePassword=password -Dsun.security.krb5.debug=true -Dsun.security.spnego.debug=true' --driver-memory 2g --driver-cores 1 --executor-memory 4g --executor-cores 1 --num-executors 3 ./sparksolr-assembly-1.0.jar zookeeper1:2181,zookeeper2:2181,zookeeper3:2181/solr-search new_solr_collection old_solr_collection |& tee spark-solr-ssl-`date -I`.log
