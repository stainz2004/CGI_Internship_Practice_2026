# CGI_Internship_Practice_2026

## Lahendus

Lõin "Nutika Restorani Reserveerimissüsteemi".

Selleks kasutasin nõutud Java Spring Boot ja viimast Java LTS versiooni. Frontend raamistikuks kasutasin Reacti koos Axiosega. Andmebaasiks kasutasin PostgreSQL ja LiquiBase tabelite haldamiseks.

- Laud -> (Laud 1, Laud 2, jne)
- Tsoon -> (VIP, Terrass, Sisesaal, Rõdu)
- Eelistus -> (Vaikne nurk, Akna all, Ratastooliga ligipääsetav, Pime nurk)

Broneering:
- Igal laual võib olla mitu broneeringut, kuid need ei tohi ajaliselt kattuda.
- Broneering kestab automaatselt 2 tundi
- Pärast broneeringu lõppu on laud uuesti saadaval
- Backend kontrollib, et broneering ei kattuks olemasoleva broneeringuga

Lisasin ka broneerimise funktsionaalsuse. Lauale peale vajutades on võimalik lauda broneerida valitud kellajaks. Broneerides jälgib backend, et broneeringu algusest kuni lõpuni (2 tundi)
ei kattuks teise broneeringuga.

Soovituste loogika arvestab mitut tegurit, sealhulgas laua tsooni, maksimaalset inimeste arvu, võimalikke eelistusi ning seda, kui hästi laud vastab broneerijate arvule. Kui kasutaja on määranud spetsiifilise eelistuse, näiteks ratastooliga ligipääsetava laua, siis antakse sellele kriteeriumile algoritmis kõrgem prioriteet, kuna selline nõue võib olla broneerija jaoks hädavajalik. Igale lauale antakse skoor vastavalt sellele kui hästi vastab filtreeringule. Kui mitu lauda on sama skooriga siis soovitatakse kõiki.

Laua filtreerimine ja soovitamine toimub täielikult backendi loogikas. Frontend ei tee arvutusi, vaid kuvab backendist saadud tulemused. Filtreerimise realiseerimiseks kasutasin Spring Data JPA Specification patternit.

Lisasin ka exceptionid juhtudeks kui broneering kattub teise broneeringuga või kui filtrite põhjal ei leitud ühtegi vastavat lauda.


## Tööks kulunud aeg ja keerulised aspektid

Selle ülesande lahendamiseks kokku läks umbes 12 tundi.

Terve projekti puhul kõige keerulisem oli struktuuri väljamõtlemine. Oli erinevaid ideid, kuidas ma andmebaasi struktureerin või
kuidas display/filter/suggestione teha. Nende puhul panin kirja erinevad variandid, kuidas oleks võimalik teha ja siis mõnel korral
arutasim ka LLM'iga, et kuidas kõige mõistlikum oleks teha.

Kõige keerulisem backendi puhul minu jaoks oli teblite ühendamine. Kuna kasutasin eraldi tabelit Reservation, siis kuidas
leida neid vahemikke millal midagi hõivatud on.

Kõige keerulisem frontendi puhul oli veebilehe disain ja ülesehitus. Selle puhul pidin tihti abi küsima LLM'ilt, et kuidas täpselt
saaksin siin sellist popupi teha (nt bookingu puhul, kui tekiberaldi aken).

---

## 🚀 Projekti käivitamise juhend

Järgnevad sammud aitavad täiesti uuel arendajal projekti kohalikult tööle saada.

---

## 1. Andmebaasi käivitamine (Docker)

Andmebaas töötab PostgreSQL-i konteineris. Käivita see käsuga:

```bash
cd backend
docker compose up -d
```

> **Kontrolli**, et Docker Desktop on käivitatud enne selle käsu jooksutamist.

---

## 2. Backend käivitamine (Spring Boot)

Backend kasutab **Java 25** ja **Spring Boot 4** koos **Gradle** ehitustööriistaga.

```bash
cd backend
./gradlew bootRun
```

Windowsil:

```bash
cd backend
gradlew.bat bootRun
```

Backend käivitub aadressil: **http://localhost:8080**

> Rakendus kasutab **Liquibase**'i, mis loob andmebaasi tabelid automaatselt käivitamisel.

---

## 3. Frontend käivitamine (React + Vite)

Frontend kasutab **React 19**, **TypeScript** ja **Vite**.

### 3.1 Sõltuvuste installimine

```bash
cd frontend
npm install
```

### 3.2 Arendusserveri käivitamine

```bash
npm run dev
```
