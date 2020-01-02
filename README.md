# Overview

Denne github, er en app lavet til Egmont Publishing A/S. Den er lavet som den afsluttende opgave for Datamatiker studiet.

## Beskrivelse

App'en skal bruges af kunder af Egmont Publishing. Kunder af Egmont Publishing modtager blade og magasiner, efter de har tegnet et abonnement. Hvis man er abonnenent og benytter sig af app'en, vil man for hvert blad man modtager, modtage et "stempel". Et stempel bliver markeret i app'en. Efter en bruger har modtaget 7 stempler (tilsvarende til 7 magasiner eller blade) vil man kunne indløse dem for at modtage point. Disse point kan bruges på at købe tilbud og gaver, i app'en. 

I app'en er der også artikler og små-tilbud. En bruger for vist artikler og tilbud ud fra et interessefelt, som bliver genereret i databasen. 

## Inden brug

App'en skal bruge internet for at virke.

For at bruge app'en, skal man logge ind. Da Egmont ikke ønskede at man skal kunne oprette sig, som ny bruger i app'en, skal man derfor bruge en pre-oprettet bruger. Herunder er en email og password som kan bruges til at logge ind med.
           email:   test@test.dk
    password:  123456
    

## Usage

Efter man er logget ind, kan man navigere rundt i app'en med en navigationsbar i bunden. Der er tre sider, Home, Offers og Profile. 

Home: Viser øverst en "fremskridtsbar", som indikere hvor mange blade eller magasiner, som en bruger mangler før at man kan modtage en gave. Derunder er en ViewPager, som viser artikler. Unden den er lokaliseret endnu en ViewPager, som viser tilbud. 

Offers: Øverst bliver antallet af point, som en bruger har akumuleret over tid. Disse point kan bruges til at købe tilbudende derunder. Tilbudende er enten farvet eller sorte/hvid afhængig om en bruger har nok point til at købe dem.

Profile: Denne side var ikke et krav for Egmont Publishing, derfor er der ikke mange funktioner endnu. Det eneste der "virker" er log-ud knappen. Ved at trykke på knappen, bliver brugeren ført tilbage til login ind skærmen. 

