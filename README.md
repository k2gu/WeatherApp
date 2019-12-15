# WeatherApp

## Arhitektuur
Antud projektis on jälgitud MVP arhitektuurimustrit.
Rakendus koosneb kolmest fragmendist ning ühest activity'st. Fragmentid on info sisestamiseks, ilma ja profiili vaatamiseks ning ilmateate ajaloo nägemiseks. Fragmente saab vahetada tänu viewPager'ile ja tab'idele. Andmete sisestamise fragment on vaates nähtav üksi, kuid tänase ilma ja profiili vaade ning ajaloovaade on nähtav valides vastava tab'i. Tab'id on nähtavad vaid juhul kui kasutaja on sisestanud oma nime ning valinud pildi.
Kogu äriloogika paikneb Presenterites, mis on testidega kaetud. Andmete vahendamisega tegelevad Repository'd.

## Andmete hoidmine
Andmeid hoitakse kahes kohas: shared preference'is ning lokaalsetes andmebaasides. Shared preference'is hoitakse kasutajaga seotud andmeid - kasutaja nimi, kasutja valitud pildi asukoht, kasutaja viimane teadaolev asukoht ning viimase pärigu tegemise kuupäev. Neid andmeid on vaja, et otsustada, kas äpi avamisel tuleks teha uus päring või mitte.
Näiteks kui kasutaja on juba täna andmeid küsinud ja ta asub samas kohas nagu viimane kord, siis ei ole mõistlik andmeid uuesti küsida. Kui aga kasutaja on vahepeal teise linnaossa sattunud, siis võib seal ilmainfo olla teistsugune.
Shared preference on samuti kasulik selleks, et otsustada, kas kasutaja tuleb äppi esimest korda või mitte. Nõuete järgi peab kasutaja saama sisestada oma nime ja pildi. Seda ei oleks aga tore iga kord küsida. Seetõttu vaatab äpp avamisel, kas tal on juba kasutaja kohta andmeid või mitte. Kui ei ole, saadab ta kasutaja enda nime ja pilti sisestama. Kui on, avab talle kohe äpi nö sisemise osa, kust saab vaadata ilmainfot.

Lokaalse baasi jaoks kasutasin Room library't. Seda seetõttu, et see teek pakub kasutajasõbralikku liidest Androidi SQLLite andmebaasile. Ka arenduses tuli ette, et tabelimudel muutus veidi. Room'i teek oskab sellega ise hakkama saada. Ilma Room'i teegita oleks arendus vaevalisem olnud, mis minu hinnangul suurt kasu ei omanud.
Lokaalses baasis on üks tabel, mis hoiab eelnevalt küsitud ilmainfot (asukoht, kuupäev ning temperatuur). 

Pildi salvestamine antud rakenduse kontekstis tähendab seda, et on salvestatud pildi viide (asukoht telefonis). Mulle ei tundunud mõistlik kopeerida pilti ennast, sest telefonis on ta olemas. Kui peaks juhtuma, et kasutaja kustutab pildi ära, siis näidatakse kasutajale nö kohahoidja pilti. Sama lugu ka siis kui kasutaja millegipärast read external storage õigused rakenduselt ära võtab.

## Suhtlus lokaalse baasiga
Lokaalse baasiga suhtlemiseks kasutab rakendus abiklassi nimega ThreadHandler. Kogu suhtlus lokaalse baasiga toimub eraldi thread'il.

## Suhtlus weatherAPIga
Ka weatherAPIga toimuv suhtlus toimub taustathread'il. Siinkohal on kasutatud AsyncTaski. Seda taski on tõenäoliselt vaja välja kutsuda kord päevas või kui kasutaja liigub ringi ja soovib uue koha ilma näha. 
WeatherAPI võti ei ole projektis kättesaadav, mistõttu ta alla tõmmates ei tohiks ka buildida. Võtit projektis kaasas ei ole, sest selle kaasa panemine oleks turvarisk. Eriti arvestades, et tegu on avaliku repositooriumiga. Tegelikult on antud juhul võti ajaloost nähtav. Ei hakanud seda peitma, sest tegu on proovitööga.

## Toetatud veajuhud
* Internetti ei ole, kui päringuid tahetakse teha
* Vastus baasist on tühi
* Vastus weatherAPIst ei õnnestu misiganes põhjusel
* Kasutaja keelab permissioni küsimise dialoogis (asukoht, salvestusruumi kasutamine)
* Kasutaja on küll varem andnud vajaminevad load, kuid seadetest on need hiljem kinni keeranud
* Kasutaja ei sisesta nime enne pildi valimist
* Kasutaja valitud pilt on misiganes põhjusel tühi
* Kasutaja lahkub pildivalikust pilti valimata

## Testid
Nagu eelnevalt mainitud, on kogu loogika presenterites testidega kaetud. Tänu sellele, on väga hea uuendusi ja muudatusi teha, sest testid kontrollivad, kuidas senikirjutatud loogika töötab.
