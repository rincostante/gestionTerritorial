gestionTerritorial
==================

Descripción de la aplicación:
-----------------------------

Esta aplicación gestiona la información correspondiente a la organización territorial de nuestro país. Contiene una API de servicios web para ser consumidos tanto por apliaciones internas como externas, de manera de proveer un registro único de Provincias, departamientos, localidades, municipios y regiones temáticas.

Se preve proximamente desacoplear el backend en un módulo EJB que proporcione la API de servicios web y una fachada que ofresca los mismos métodos para consumo mediante JNDI.

La acreditación de esta aplicación es gestionada mediante el servicio brindado por gestionAplicaciones.

Disitingue dos roles de usuarios, el administrador que gestiona los contenidos y el seguridad, que gestiona usuarios y roles. Para asignar usuarios a la aplicación, previamente deben ser vinculados mediante gestionAplicaciones.	

Para más datos se recomienda acceder a la documentación de la aplicación en \\vmfs\Desarrollo\Servicios\Gestion Territorial


Ambiente:
---------

Para conocer las características del ambiente para el despliegue de la aplicación se recomiendo leer la documentación alojada en \\vmfs\Desarrollo\Aplicaciones\javaApp. Dado que la aplicación requiere el servicio de acreditación de usuarios de gestionAplicaciones, es condición para el ambiente local de esta aplicación tener desplegada previamente gestionAplicaciones. Una vez levantado el proyecto en el IDE, con gestionAplicaciones despleagada, deberá actualizarse la referencia a http://localhost:8080/AccesoAppWebService/AccesoAppWebService?WSDL para que se creen las entidades necesarias para el consumo del servicio.


Configuraciones:
----------------

Dado que todas las dependencias de las aplicaciones desarrolladas en java están gestionadas por Maven, una vez levantado el proyecto, deberá actualizarse todas las dependencias mediante el comando respectivo del IDE para que pueda compilar sin inconvenientes.

Deberá generarse el recurso JDBC correspondiente al acceso a datos, según los jndi-name que se especifican en el archivo glassfish-resources.xml. El jta-data-source de la unidad de persistencia que se encuentra en el archivo persistence.xml deberá ser centrosPobladosNDI

Las credenciales de acceso al servidor de base de datos, por defecto serán: us=postgres, pass=root, en cualquier otro caso, estos datos deberán sobreescribirse en el archivo glassfish-resource.xml.

El archivo Bundle.properties, contiene los datos de server, la ruta de la aplicación para autenticar y nombres de las cookies a leer.


Datos:
------

Deberá crearse la base de datos gestionTerritorial en el servidor local de Postgres y los permisos según se especifica en el archivo glassfish-resource.xml
	
Los backup de la base se encuentran en \\vmfs\Desarrollo\Servicios\Gestion Territorial\bkBase y los scripts en \\vmfs\Desarrollo\Servicios\Gestion Territorial\scriptsBase
	
Se recomienda crear, luego de la base, primero las tablas y luego las restricciones de cada una.


Servicios brindados:
--------------------
	
Los archivos xml correspondientes al contrato del servicio web que brinda la aplicación para gestionar el logeo de otras aplicaciones del mismo entorno, se encuentran en \\vmfs\Desarrollo\Servicios\Gestion Territorial\contratoServicios


Servicios consumidos:
---------------------
	
Como se antició, la aplicación consume los servicios de acreditación de usuarios de gestionAplicaciones.
