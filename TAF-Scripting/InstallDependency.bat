call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Web-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Web -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Mobile-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Mobile -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-WebServices-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-WebServices -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Desktop-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Desktop -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Accessibility-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Accessibility -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-GenericUtility-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-GenericUtility -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-Reporting-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-Reporting -Dversion=0.0.3 -Dpackaging=jar

call mvn install:install-file -Dfile=${project.basedir}/libs/TAF-UILayout-0.0.3.jar -DgroupId=TAF -DartifactId=TAF-UILayout -Dversion=0.0.3 -Dpackaging=jar