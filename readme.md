NAVY MANAGER - A PROJEKT

Hadiflotta menedzsment

FUTTATÁS:

    Legegyszerűbb mód a "docker compose up" parancs kiadása a program gyökérkönytárában, 
    így saját PostgreSQL adatbázissal dolgozhat a applikáció.
    
    Ha a Maven Flyway Plugin parancsokat akarod futtatni: "mvn flyway:clean" és "mvn flyway:migrate" akkor 
    előtte futtasd a "flyway_maven_plugin_envs" .bat avagy .sh állományt, 
    ezekben az adatbázis beállítások át is írhatóak.

    Ha a Maven Spring Boot Plugint akarod használni az alkalmazás futtatására: "mvn spring-boot:run" akkor 
    előtte futtasd a "maven_spring_boot_plugin_envs" .bat avagy .sh állományt, 
    ezekben az adatbázis beállítások át is írhatóak.

TESZTELÉS:

    Add ki a "mvn test" parancsot, a teszt H2 memória adatbázist használ, valamint a Flyway ki van kapcsolva, 
    így nem szükséges környezeti változók megadása.

LEÍRÁS:

    A program teljes webes felülettel rendelkezik, valamint az adatcserét végrehajtó végpontok REST architektúrát használnak, 
    és JSON formátumban várják-küldik az adatokat. 
    Ilyen módon grafikus és programozható módon is könnyen elérhető minden funkcionalitás.
    A webes űrlapok is JSON-t használnak, Ajax segítségével eljuttatva ezeket a szerverre.

    Adattárolás PostgreSQL adatbázisban történik.
    Az adatbázis egyed-kapcsolat diagramja megtalálható a projekt könytárában "NavyManagerERDiagram.png" néven.
    
    Spring Security védi módosítás ellen végpontokat, csak a "/login" végponton lehet kizárólag "POST"-olt 
    belépési információt küldeni a belépéshez. 
    Egyetlen felhasználó van: "Admin", jelszava: "security", csak ő tud adatokat módosítani, 
    a webes felület figyeli is a felhasználói szerepet, csak az "ADMIN" szerep látja a módosító gombokat és az id-ket.
    A védelem szerveroldali, így a gombok "meghekkelése" nem segít, "Bad credentials!" üzenettel jutalmaz.
    "GET" minden végpontra engedélyezett, mivel így bizalmas információ nem elérhető.
    A nem belépett felhasználó "Guest" néven van emlegetve, ő csak adatokat nézegetni, keresni tud.
    
    Ha entitásnak van "Country" mezője, ez kötelezően beállítandó, és le is korlátoz minden egyéb 
    kapcsolatot az azonos országból származó entitásokra.
    (Pl.: azonos országból jöhet csak a kapitány és a hajó, stb.)

    Egy tiszt csak egy hajó avagy flotta parancsnoka lehet.
    Egy hajót csak akkor lehet hozzáadni egy flottához, ha van parancsnoka.
    Egy hajótípushoz tartozó ágyúk típusaik alapján képeznke ehhez a hajótípushoz tartozó ágyútípus-mennyiség entitásokat,
    tehát egy bizonyos ágyútípus csak egyszer szerepelhet egy bizonyos mennyiséggel egy hajótípus esetében.

VÉGPONTOK:
    Ha a végpont NEM adatot kérdez le, akkor "JsonResponse" formájában válaszol, ennek mezői:
        String message :            nyugtázó üzenet, hiba esetén null az értéke
        String errorDescription :   hibaüzenet, ha nincsen hiba akkor null az értéke. 
                                    A kivételekben van beállítva az üzenet, a "ControllerAdvice"-al
                                    annotált osztályok küldik vissza ResponseEntity formájában.
        Map<String, String> errorMessages;
                                    hibaüzenet, ha nincsen hiba, akkor null az értéke.
                                    Itt vannak összegyűjte a validációs hibák a "BindingResult"-ból,
                                    mezők nevei és hibaüzenetei formájában.

    Country REST: mapping -> "/country"
        GET:                    Listázza az összes országot
            visszaad: List<CountryDto> 
        GET: /{id}              Lekérdez egy már létező országot
            visszaad: CountryDto
        POST:                   CountryDto request body formájában hozzáadd az adatbázishoz egy országot
            visszaad: JsonResponse
        PUT: /{id}              CountryDto request body formájában frissíti egy már létező ország adatait.
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező országot
            visszaad: JsonResponse

    Country Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot ország hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot ország frissítésére
        GET : /{id}/show-details-page    Megmutatja az országot részletező weblapot
        GET : /show-list-page           Megmutatja az összes országot listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Rank REST: mapping -> "/rank"
        GET:                    Listázza az összes rangot
            visszaad: List<RankDto>
        GET: /{id}              Lekérdez egy már létező rangot
            visszaad: RankDto
        POST:                   RankDto request body formájában hozzáadd az adatbázishoz egy rangot
            visszaad: JsonResponse
        PUT: /{id}              RankDto request body formájában frissíti egy már létező rang adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező rangot
            visszaad: JsonResponse

    Rank Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot rang hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot rang frissítésére
        GET : /{id}/show-details-page    Megmutatja az rangot részletező weblapot
        GET : /show-list-page           Megmutatja az összes rangot listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Officer REST: mapping -> "/officer"
        GET:                    Listázza az összes tisztet
            visszaad: List<OfficerDto>
        GET: /{id}              Lekérdez egy már létező tisztet
            visszaad: OfficerDto
        POST:                   OfficerDto request body formájában hozzáadd az adatbázishoz egy tisztet
            visszaad: JsonResponse
        PUT: /{id}              OfficerDto request body formájában frissíti egy már létező tiszt adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező tisztet
            visszaad: JsonResponse

    Officer Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot tiszt hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot tiszt adatainak frissítésére
        GET : /{id}/show-details-page    Megmutatja az tiszt adatait részletező weblapot
        GET : /show-list-page           Megmutatja az összes tisztet listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Gun REST: mapping -> "/gun"
        GET:                    Listázza az összes ágyút
            visszaad: List<GunDto>
        GET: /{id}              Lekérdez egy már létező ágyút
            visszaad: GunDto
        POST:                   GunDto request body formájában hozzáadd az adatbázishoz egy ágyút
            visszaad: JsonResponse
        PUT: /{id}              GunDto request body formájában frissíti egy már létező ágyú adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező ágyút
            visszaad: JsonResponse

    Gun Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot ágyú hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot ágyú adatainak frissítésére
        GET : /{id}/show-details-page    Megmutatja az ágyút részletező weblapot
        GET : /show-list-page           Megmutatja az összes ágyút listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Hull Classification REST: mapping -> "/hull-classification"
        GET:                    Listázza az hajótest-besorolást
            visszaad: List<HullClassificationDto>
        GET: /{id}              Lekérdez egy már létező hajótest-besorolást
            visszaad: HullClassificationDto
        POST:                   HullClassificationDto request body formájában hozzáadd az adatbázishoz egy hajótest-besorolást
            visszaad: JsonResponse
        PUT: /{id}              HullClassificationDto request body formájában frissíti egy már létező hajótest-besorolás adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező hajótest-besorolást
            visszaad: JsonResponse

    Hull Classification Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot hajótest-besorolás hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot hajótest-besorolás adatainak frissítésére
        GET : /{id}/show-details-page    Megmutatja az hajótest-besorolást részletező weblapot
        GET : /show-list-page           Megmutatja az összes hajótest-besorolást listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Ship Class REST: mapping -> "/ship-class"
        GET:                    Listázza az összes hajóosztályt
            visszaad: List<ShipClassDto>
        GET: /{id}              Lekérdez egy már létező hajóosztályt
            visszaad: ShipClassDto
        POST:                   ShipClassDto request body formájában hozzáad az adatbázishoz egy hajóosztályt
            visszaad: JsonResponse
        PUT: /{id}              ShipClassDto request body formájában frissíti egy már létező hajóosztály adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező hajóosztályt
            visszaad: JsonResponse
        POST: /{id}/gun         GunInstallationDto request body formájában egy ágyútípus-mennyiség entitást
                                ad hozzá a hajótípushoz
            visszaad: JsonResponse
        GET: /{id}/gun          Listázza a hajóosztályhoz tartozó ágyútípus-mennyiség entitásokat
            visszaad: List<GunInstallationDto>
        GET: /{shipClassId}/gun/{gunId}     Lekérdezi a hajótípus egy már létező ágyútípus-mennyiség entitását
            visszaad: GunInstallationDto
        PUT: /{shipClassId}/gun/{gunId}     GunInstallationDto request body formájában egy ágyútípus-mennyiség entitást
                                            frissít
            visszaad: JsonResponse
        DELETE: /{shipClassId}/gun/{gunId}   Törli a hajótípus egy ágyútípus-mennyiség entitását, az entitás
                                             nem létezhet hajótípus nélkül, így végleges törlésre kerül.

    Ship Class Frontend:
        GET : /show-add-form                 Megmutatja az webes űrlapot hajóosztály hozzáadására    
        GET : /{id}/show-update-form         Megmutatja az webes űrlapot hajóosztály adatainak frissítésére
        GET : /{id}/show-details-page        Megmutatja az hajóosztályt részletező weblapot
        GET : /show-list-page                Megmutatja az összes hajóosztályt listázó weblapot
        GET : /{id}/gun/show-add-gun-form    Megmutatja az webes űrlapot ágyútípus-mennyiség entitás hajóosztályhoz 
                                             való hozzáadására
        GET : /{shipClassId}/gun/{gunId}/show-update-gun-form    Megmutatja az webes űrlapot hajóosztály 
                                                                 ágyútípus-mennyiség entitásának frissítésére

------------------------------------------------------------------------------------------------------------------------

    Ship REST: mapping -> "/ship"
        GET:                    Listázza az összes hajót
            visszaad: List<ShipDto>
        GET: /{id}              Lekérdez egy már létező hajót
            visszaad: ShipDto
        POST:                   ShipDto request body formájában hozzáad az adatbázishoz egy hajót
            visszaad: JsonResponse
        PUT: /{id}              ShipDto request body formájában frissíti egy már létező hajó adatait
            visszaad: JsonResponse
        DELETE: /{id}           töröl egy már létező hajót
            visszaad: JsonResponse

    Ship Frontend:
        GET : /show-add-form            Megmutatja az webes űrlapot hajó hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot hajó adatainak frissítésére
        GET : /{id}/show-details-page    Megmutatja az hajót részletező weblapot
        GET : /show-list-page           Megmutatja az összes hajót listázó weblapot

------------------------------------------------------------------------------------------------------------------------

    Fleet REST: mapping -> "/fleet"
        GET:                     Listázza az összes flottát
            visszaad: List<FleetDto>
        GET: /{id}               Lekérdez egy már létező flottát
            visszaad: FleetDto
        POST:                    FleetDto request body formájában hozzáad az adatbázishoz egy flottát
            visszaad: JsonResponse
        PUT: /{id}               FleetDto request body formájában frissíti egy már létező flotta adatait
            visszaad: JsonResponse
        DELETE: /{id}            Töröl egy már létező flottát
            visszaad: JsonResponse
        POST: /{id}/ship         IdentityDto request body formájában egy létező hajót ad hozzá a flottához.
            visszaad: JsonResponse
        GET: /{id}/ship          Listázza a flottához tartozó hajókat
            visszaad: List<ShipDto>
        GET: /{fleetId}/ship/{shipId}     Lekérdez egy a flottához tartozó hajót
            visszaad: ShipDto
        PUT: /{fleetId}/ship/{shipId}     IdentityDto request body formájában új hajó kerül egy régi helyére a flottában 
            visszaad: JsonResponse
        DELETE: /{fleetId}/ship/{shipId}  Eltávolít egy hajót a flottából
            visszaad: JsonResponse

    Fleet Frontend:
        GET : /show-add-form             Megmutatja az webes űrlapot flotta hozzáadására    
        GET : /{id}/show-update-form     Megmutatja az webes űrlapot flotta adatainak frissítésére
        GET : /{id}/show-details-page    Megmutatja az flottát részletező weblapot
        GET : /show-list-page            Megmutatja az összes flottát listázó weblapot
        GET : /{id}/ship/show-add-ship-form    Megmutatja az webes űrlapot egy létező hajó flottához való adásához
        GET : /{shipClassId}/ship/{shipId}/show-update-ship-form     Megmutatja az webes űrlapot a flotta 
                                                                    egy hajójának frissítésére