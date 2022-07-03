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

    Add ki a "mvn test" parancsot, a teszt H2 memória adatbázist használ, és a Flyway ki van kapcsolva, 
    így nem szükséges környezeti változók megadása.

LEÍRÁS:

    A program teljes webes felülettel rendelkezik, a végpontok pedig REST architektúrát használnak, 
    és JSON formátumban várják az adatokat. 
    Ilyen módon grafikus és programozható módon is könnyen elérhető minden funkcionalitás.
    A webes űrlapok is JSON-t használnak, Ajax segítségével eljuttatva ezeket a szerverre.

    Adattárolás PostgreSQL adatbázisban történik.
    
    Spring Security védi módosítás ellen végpontokat, csak a "/login" végponton lehet kizárólag "POST"-olt 
    belépési információt küldeni a belépéshez. 
    Egyetlen felhasználó van: "Admin", jelszava: "security", csak ő tud adatokat módosítani, 
    a webes felület figyeli is a felhasználói szerepet, csak az "ADMIN" szerep látja a módosító gombokat.
    A védelem szerveroldali, így a gombok "meghekkelése" nem segít, "Bad credentials!" üzenettel jutalmaz.
    "GET" minden végpontra engedélyezett, mivel így bizalmas információ nem elérhető.
    A nem belépett felhasználó "Guest" néven van emlegetve, ők csak adatokat nézegetni, keresni tudnak.
    
    Ha entitásnak van "Country" mezője, ami ez kötelezően beállítandó, és le is korlátoz minden egyéb 
    kapcsolatot az azonos országból származó entitásokra.
    (Pl.: azonos országból jöhet csak a kapitány és a hajó, stb.)

    Egy tiszt csak egy hajó avagy flotta parancsnoka lehet.
    Egy hajót csak akkor lehet hozzáadni egy flottához, ha van parancsnoka.

VÉGPONTOK:

    PRODUCT:
        POST: /product - új Product hozzáadás 
                         Requestbody: ProductDTO
        GET: /product - összes Product kilistázása
        GET: /product/name/{name} - adott nevű Product-ok listáját adja vissza
        GET: /product/type/{type} - adott típusú Product-okat listázza
                                    type: ProductType enum megfelelő String változata
        GET: /product/status/{status} - adott státuszban lévő product-okat listázza
                                    status: ProductStatus enum String változata
        GET: /product/date/{date} - adott dátumon módosított product-okat listázza
                                    date: YYYY-MM-DD formátumban
        GET: /product/id/{id} - adott ID-vel rendelkező Product-al tér vissza
        PUT: /product/id/{id} - adott ID-ű Product adatait változtatja
                                Requestbody: ProductDTO
        DELETE: /product/id/{id} - adott ID-ű Product-ot törli
    
    WAREHOUSE:
        POST: /warehouse - új Warehouse hozzáadás
                           Requestbody: WarehouseDTOWithoutId
        GET: /warehouse - összes Warehouse listázása
        GET: /warehouse/id/{id} - adott ID-ű Warehouse-t adja vissza
        GET: /warehouse/name/{name} - adott nevű Warehouse-t adja vissza
        GET: /warehouse/address/{address} - adott címen lévő Warehouse-t adja vissza
                                    address: 1111 Település név Utca név 22 formátum
        GET: /wrehouse/warehouse/{id} - adott ID-ű Warehouse Product-jait listázza
        GET: /warehouse/workers_needed - összes Warehouse-t listázza hiányzó munkások
                                        alapján sorba rendezve
        PUT: /warehouse/id/{id} - adott ID-ű Warehouse adatait változtatja
                                  Requestbody: WarehouseDTOWithoutId
        Delete: /warehouse/id/{id} - adott ID- ű Warehouse-t törli

    WORKER:
        POST: /worker - új Worker hozzáadás
                        Requestbody: WorkerDTO
        GET: /worker - összes Worker listázása
        GET: /worker/id/{id} - adott ID-ű Worker-t adja vissza
        GET: /worker/position/{position} - adott Position-ben dolgozó Worker-ek
                                           listázása
                                        position: WorkPosition enum String változata
        GET: /worker/salary/{id}/month/{month} - adott ID-ű Worker jelenlegi év
                                                 adott hónapban keresett pénze 
                                                 Double-ként
                                                 month: MM formátumban
        GET: /worker/salary/{id}/start/{start}/end/{end} - adott ID-ű Worker
                                                           két dátum közötti keresete
                                                           Double-ként
                                                    start/end: YYYY-MM-DD formátumban
        POST: /worker/work/id/{id}/warehouse/{warehouse_id}/hours/{hours} - 
                                                    adott ID-ű Worker-hez
                                                    az adott ID-ű Warehouse-ban
                                                    adott órányi munka hozzáadása
                                                    aktuális napra
        GET: /worker/work/{id} - adott ID-ű Worker összes munkanapjának listázáta
        PUT: /worker/id/{id} - adott ID-ű Worker adatainak változtatása
                               Requestbody: WorkerDTO
        DELETE: /worker/id/{id} - adott ID-ű Worker törlése