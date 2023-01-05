pipeline {

    agent any

    stages {
        stage('Load testing') {
            steps {
                configFileProvider([configFile(fileId: '267423b6-76ad-4e54-a7c2-222fafbf7fb9', variable: 'TRUST_PEM')]) {
                withCredentials([certificate(credentialsId: 'TSTNMT2321000156-B02', keystoreVariable: 'CERTKEY', passwordVariable: 'CERTKEYPWD')]) {
                    sh """
                        #! /bin/bash
                        echo "Tests will be run against the following host: ${TARGETHOST}"
                        cd loadtest

                        rm -fr ./conf/truststore.p12
                        csplit -z -f ./conf/trust- $TRUST_PEM '/-----END CERTIFICATE-----/+1' '{*}' 
                        ls ./conf/trust-*| xargs -I{} keytool -J-Dkeystore.pkcs12.legacy -importcert -storetype PKCS12 -keystore ./conf/truststore.p12 -storepass banan69 -file {} -alias {} -noprompt

                        cat ${CERTKEY} > ./conf/cert.p12
                        ls -l ./conf/cert.p12
                        sed -e 's@KEYSTOREVARIABLE@'"cert.p12"'@; s@KEYSTOREPASSWORD@'"${CERTKEYPWD}"'@' ./conf/gatling.sedit > ./conf/gatling.conf
                        sed -i -e 's@TRUSTSTOREVARIABLE@'"truststore.p12"'@; s@TRUSTSTOREPASSWORD@'"banan69"'@' ./conf/gatling.conf
                        sed -e 's@SIMULATION@'"RequestsNTjP"'@' ./user-files/chown-files.sedit > ./user-files/chown-files.sh
                        sed -e 's@TARGETHOST@'"${TARGETHOST}"'@' ./user-files/simulations/RequestsNTjP.sedit > ./user-files/simulations/RequestsNTjP.scala
                        docker compose run --rm testsuite
                    """
                }}
                gatlingArchive()
            }
        }
    }
    post {
        always {
            junit 'loadtest/results/**/assertions.xml'
        }
    }
}
