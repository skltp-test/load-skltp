pipeline {

    agent any

    stages {
        stage('Load testing') {
            steps {
                withCredentials([certificate(credentialsId: 'TSTNMT2321000156-B02', keystoreVariable: 'CERTKEY', passwordVariable: 'CERTKEYPWD'),
                                 certificate(credentialsId: 'TSTNMT_TRUSTSTORE', keystoreVariable: 'TRUSTKEY', passwordVariable: 'TRUSTKEYPWD')]) {
                    sh """
                        #! /bin/bash
			echo "Tests will be run against the following host: ${TARGETHOST}"
                        keytool -list -keystore ${TRUSTKEY} -storepass ${TRUSTKEYPWD} -storetype pkcs12
                        cd loadtest
                        cat ${CERTKEY} > ./conf/cert.p12
                        ls -l ./conf/cert.p12
                        sed -i -e 's@KEYSTOREVARIABLE@'"cert.p12"'@; s@KEYSTOREPASSWORD@'"${CERTKEYPWD}"'@' ./conf/gatling.conf
                        cat ${TRUSTKEY} > ./conf/truststore.p12
                        ls -l ./conf/truststore.p12
                        sed -i -e 's@TRUSTSTOREVARIABLE@'"truststore.p12"'@; s@TRUSTSTOREPASSWORD@'"${TRUSTKEYPWD}"'@' ./conf/gatling.conf
			sed -i -e 's@TARGETHOST@'"${TARGETHOST}"'@' ./user-files/simulations/RequestsNTjP.scala
                        docker-compose run --rm testsuite
                    """
                }
                gatlingArchive()
            }
        }
    }
}
