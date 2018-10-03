pipeline {

    agent any

    stages {
        stage('Load testing') {
            steps {
                withCredentials([certificate(credentialsId: 'TSTNMT2321000156-B02', keystoreVariable: 'CERTKEY', passwordVariable: 'CERTKEYPWD'),
                                 certificate(credentialsId: 'TSTNMT_TRUSTSTORE', keystoreVariable: 'TRUSTKEY', passwordVariable: 'TRUSTKEYPWD')]) {
                    sh """
                        #! /bin/bash
                        keytool -list -keystore ${TRUSTKEY} -storepass ${TRUSTKEYPWD} -storetype pkcs12
                        cd loadtest
                        cat ${CERTKEY} > ./conf/cert.p12
                        ls -l ./conf/cert.p12
                        sed -i -e 's@KEYSTOREVARIABLE@'"cert.p12"'@; s@KEYSTOREPASSWORD@'"${CERTKEYPWD}"'@' ./conf/gatling.conf
                        cat ${TRUSTKEY} > ./conf/truststore.p12
                        ls -l ./conf/truststore.p12
                        sed -i -e 's@TRUSTSTOREVARIABLE@'"truststore.p12"'@; s@TRUSTSTOREPASSWORD@'"${TRUSTKEYPWD}"'@' ./conf/gatling.conf
                        docker-compose run --rm testsuite 
                    """
                }
                gatlingArchive()
            }
        }
    }

    post {
        always {
            
            // Archive soaptest results
            junit healthScaleFactor: 100.0, testResults: 'loadtest/TEST*.xml'
            
        }
    }
}
