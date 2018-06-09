# d.repository.neo4j

Implementation to GraphDataBaseService to provide managed connection to neo4j connection.

The plugin requires configuration to start as the following:
- service id: media.dee.dcms.db.neo4j.service
- properties are:
    - username: neo4j username
    - password: neo4j password
    - url: Neo4j URL Connection
    - sessionMax: Neo4j Session Max
    - livenessCheckout: Neo4j Connection Liveness Check Timeout
    
    
## Dependencies
Neo4j module requires the following OSGI modules to be installed.
- neo4j driver: install using command `bundle:install install mvn:org.neo4j.driver/neo4j-java-driver/1.4.4`
   
