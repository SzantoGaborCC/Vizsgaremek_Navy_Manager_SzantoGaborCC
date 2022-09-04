NAVY MANAGER - THE PROJECT

TO RUN THE PROJECT:

    The easiest way is to issue the "docker compose up" command in the root directory of the program,
    so the application can work with its own PostgreSQL database. Then the main page is available at "localhost:8080".
    Spring Security username: "Admin", password: "security".
    
    If you want to run the Maven Flyway Plugin commands: "mvn flyway:clean" and "mvn flyway:migrate" then
    before that you have to run the "flyway_maven_plugin_envs" .bat or .sh file,
    the database settings can also be overwritten in these files.

    If you want to use the Maven Spring Boot Plugin to run the application: "mvn spring-boot:run" then
    before that, run the "maven_spring_boot_plugin_envs" .bat or .sh file,
    the database settings can also be overwritten in these files.

TO TEST THE PROJECT:

    Issue the "mvn test" command, the test uses the H2 memory database, and Flyway is turned off,
    so it is not necessary to specify environment variables.

PROJECT DESCRIPTION:

    The program has a full web interface, and the endpoints performing data exchange use a REST architecture,
    and they expect and send the data in JSON format.
    In this way, all functionality is easily accessible in a graphical and programmable way.
    Web forms also use JSON, sending them to the server using Ajax.
    Swagger documentation for endpoints is available at localhost:8080/swagger-ui.html.

    Data is stored in a PostgreSQL database.
    The entity-relationship diagram of the database can be found in the project directory under the name "NavyManagerERDiagram.png".
    
    Spring Security protects endpoints against modification, "POST" can can be done on the "/login" endpoint
    to send login information, in order to log out just an empty "POST" must be sent to the "/logout" endpoint.
    There is only one user: "Admin", the password is: "security", only this user can modify data.
    Security also monitors the user role, only the "ADMIN" role sees the modifying buttons and ids.
    The protection is server-side, so "hacking" the buttons doesn't help, the "Bad credentials!" message is reward for that.
    "GET" is allowed for all endpoints, as this way confidential information is not available.
    The user who has not logged in is referred to as "Guest", he can only view and search data.
    
    If an entity has a "Country" field, it must be set, and it also limits everything else
     to entities from the same country.
    (E.g.: the captain and the ship have to come from the same country, etc.)

    An officer can only be in command of one ship or fleet.
    A ship can only be added to a fleet if it has a commander.
    Cannons belonging to a ship type, form the "cannon type-quantity" entities belonging to this ship type,
    so a certain cannon type can only appear once with a certain quantity for a ship type.

ENDPOINTS:

    If the endpoint does NOT request data, it responds in the form of a "JsonResponse", its fields are:
        String message: confirmation message, null in case of error
        String errorDescription: error message, if there is no error, its value is null.
            In exceptions, the message is set with "ControllerAdvice" annotated classes 
            returned in the form of a ResponseEntity.
        Map<String, String> errorMessages: error message, if there is no error, the value is null.
        The validation errors from "BindingResult" are collected here,
        in the form of field names and error messages.

    Country REST: mapping -> "/country/api"
        GET: List all countries
            returns List<CountryDto>
        GET: /{id} Queries an already existing country
            returns CountryDto
        POST: Add a country to the database in the form of CountryDto request body
            returns JsonResponse
        PUT: /{id} CountryDto updates the data of an already existing country in the form of a request body.
            returns JsonResponse
        DELETE: /{id} deletes an existing country
            returns JsonResponse

    Country Frontend: mapping -> "/country"
        GET : /show-add-form Shows the web form to add a country
        POST : /add-with-form Sends the CountryDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for country update
        PUT : /update-with-form Forwards the CountryDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the webpage detailing the country
        GET : /show-list-page Shows the web page listing all countries

------------------------------------------------------------------------------------------------------------------------

    Rank REST: mapping -> "/rank/api"
        GET: List all ranks
            returns List<RankDto>
        GET: /{id} Queries an existing rank
            returns: RankDto
        POST: Add a rank to the database in the form of RankDto request body
            returns JsonResponse
        PUT: /{id} RankDto updates the data of an already existing rank in the form of a request body
            returns JsonResponse
        DELETE: /{id} deletes an existing rank
            returns JsonResponse

    Rank Frontend: mapping -> "/rank"
        GET : /show-add-form Shows the web form to add rank
        POST : /add-with-form Sends the RankDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for rank update
        PUT : /update-with-form Forwards the RankDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the web page detailing the rank
        GET : /show-list-page Shows the web page listing all ranks

------------------------------------------------------------------------------------------------------------------------

    Officer REST: mapping -> "/officer/api"
        GET: List all officers
            returns List<OfficerDto>
        GET: /{id} Queries an existing officer
            returns OfficerDto
        POST: Add an officer to the database in the form of OfficerDto request body
            returns JsonResponse
        PUT: /{id} Updates the data of an already existing officer in the form of OfficerDto request body
            returns JsonResponse
        DELETE: /{id} deletes an existing officer
            returns JsonResponse

    Officer Frontend: mapping -> "/officer"
        GET : /show-add-form Shows the web form to add an officer
        POST : /add-with-form Passes the OfficerDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for updating officer details
        PUT : /update-with-form Forwards the OfficerDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the officer's details page
        GET : /show-list-page Shows the web page listing all officers

------------------------------------------------------------------------------------------------------------------------

    Gun REST: mapping -> "/gun/api"
        GET: List all cannons
            returns List<GunDto>
        GET: /{id} Queries an existing cannon
            returns: GunDto
        POST: Add a cannon to the database in the form of a GunDto request body
            returns JsonResponse
        PUT: /{id} GunDto updates the data of an existing gun in the form of a request body
            returns JsonResponse
        DELETE: /{id} deletes an existing cannon
            returns JsonResponse

    Gun Frontend: mapping -> "/gun"
        GET : /show-add-form Shows the web form to add a cannon
        POST : /add-with-form Sends the GunDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for updating cannon data
        PUT : /update-with-form Forwards the GunDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the cannon details page
        GET : /show-list-page Shows the web page listing all cannons

------------------------------------------------------------------------------------------------------------------------

    Hull Classification REST: mapping -> "/hull-classification/api"
        GET: List the hull classification
            returns List<HullClassificationDto>
        GET: /{id} Queries an existing hull classification
            returns: HullClassificationDto
        POST: Add a hull classification to the database in the form of HullClassificationDto request body
            returns JsonResponse
        PUT: /{id} HullClassificationDto updates the data of an existing hull classification in the form of a request body
            returns JsonResponse
        DELETE: /{id} deletes an existing hull classification
            returns JsonResponse

    Hull Classification Frontend: mapping -> "/hull-classification"
        GET : /show-add-form Shows the web form to add hull classification
        POST : /add-with-form Passes the HullClassificationDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for updating hull classification data
        PUT : /update-with-form Passes the HullClassificationDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the hull classification detail page
        GET : /show-list-page Shows the web page listing all hull classifications

------------------------------------------------------------------------------------------------------------------------

    Ship Class REST: mapping -> "/ship-class/api"
        GET: List all ship classes
            returns List<ShipClassDto>
        GET: /{id} Queries an existing ship class
            returns: ShipClassDto
        POST: ShipClassDto adds a ship class to the database in the form of a request body
            returns JsonResponse
        PUT: /{id} ShipClassDto updates the data of an existing ship class in the form of a request body
            returns JsonResponse
        DELETE: /{id} deletes an existing ship class
            returns JsonResponse
        POST: /{id}/gun GunInstallationDto request body as a gun type quantity entity
                                adds to the ship type
            returns JsonResponse
        GET: /{id}/gun List the gun type quantity entities for the ship class
            returns List<GunInstallationDto>
        GET: /{shipClassId}/gun/{gunId} Queries the ship type for an existing gun type quantity entity
            returns GunInstallationDto
        PUT: /{shipClassId}/gun/{gunId} GunInstallationDto request body as a gun type quantity entity
                                            refresh
            returns JsonResponse
        DELETE: /{shipClassId}/gun/{gunId} Deletes a gun type quantity entity of the ship type, the entity
                                             it cannot exist without a ship type, so it will be permanently deleted.

    Ship Class Frontend: mapping -> "/ship-class"
        GET : /show-add-form Shows the web form for adding a ship class
        GET : /{id}/show-update-form Shows the web form for updating ship class data
        GET : /{id}/show-details-page Shows the web page detailing the ship class
        GET : /show-list-page Shows the web page listing all ship classes
        GET : /{id}/gun/show-add-gun-form Shows the web form for gun type quantity entity ship class
                                             to add
        POST : /{id}/gun Passes the GunInstallationDto request body to the API for addition
            returns JsonResponse
        GET : /{shipClassId}/gun/{gunId}/show-update-gun-form Shows the web form ship class
                                                                 to update the cannon type quantity entity
        POST : /{id}/gun/{gunId} Passes GunInstallationDto request body to API for update
            returns JsonResponse

------------------------------------------------------------------------------------------------------------------------

    Ship REST: mapping -> "/ship/api"
        GET: List all ships
            returns List<ShipDto>
        GET: /{id} Queries an existing ship
            returns: ShipDto
        POST: ShipDto adds a ship to the database in the form of a request body
            returns JsonResponse
        PUT: /{id} ShipDto updates the data of an existing ship in the form of a request body
            returns JsonResponse
        DELETE: /{id} deletes an existing ship
            returns JsonResponse

    Ship Frontend: mapping -> "/ship"
        GET : /show-add-form Shows the web form to add a ship
        POST : /add-with-form Sends the ShipDto request body to the API for addition
            returns JsonResponse
        GET : /{id}/show-update-form Shows the web form for updating ship information
        PUT : /update-with-form Forwards the ShipDto request body to the API for updating
            returns JsonResponse
        GET : /{id}/show-details-page Shows the web page detailing the ship
        GET : /show-list-page Shows the web page listing all ships

------------------------------------------------------------------------------------------------------------------------

    Fleet REST: mapping -> "/fleet/api"
        GET: List all fleets
            returns List<FleetDto>
        GET: /{id} Queries an existing fleet
            returns: FleetDto
        POST: FleetDto adds a fleet to the database in the form of a request body
            returns JsonResponse
        PUT: /{id} Updates the data of an already existing fleet in the form of a FleetDto request body
            returns JsonResponse
        DELETE: /{id} Deletes an existing fleet
            returns JsonResponse
        POST: /{id}/ship Adds an existing ship to the fleet as an IdentityDto request body.
            returns JsonResponse
        GET: /{id}/ship List the ships belonging to the fleet
            returns List<ShipDto>
        GET: /{fleetId}/ship/{shipId} Queries a ship belonging to the fleet
            returns: ShipDto
        PUT: /{fleetId}/ship/{shipId} In the form of IdentityDto request body, a new ship replaces an old one in the fleet
            returns JsonResponse
        DELETE: /{fleetId}/ship/{shipId} Removes a ship from the fleet
            returns JsonResponse

    Fleet Frontend: mapping -> "/fleet"
        GET : /show-add-form Shows the web form for adding a fleet
        GET : /{id}/show-update-form Shows the web form for updating fleet information
        GET : /{id}/show-details-page Shows the web page detailing the fleet
        GET : /show-list-page Shows the web page listing all fleets
        GET : /{id}/ship/show-add-ship-form Shows the web form to add an existing ship to the fleet
        POST : /{id}/ship/add-with-form Sends the id of the selected ship to the API for addition
            returns JsonResponse
        GET : /{shipClassId}/ship/{shipId}/show-update-ship-form Shows the web form for the fleet
                                                                     to upgrade a ship
        PUT : /{fleetId}/ship/{shipId}/show-update-ship-form Sends the id of the selected ship to the API for updating
            returns JsonResponse