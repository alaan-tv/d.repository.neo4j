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
   
