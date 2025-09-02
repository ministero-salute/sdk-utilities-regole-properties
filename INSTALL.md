# 📦 Guida all'Installazione degli SDK MdS

Questa guida descrive i passaggi necessari per l'installazione di un **SDK per l'invio e validazione dei dati verso MdS**, comprendente:
- Dipendenze necessarie `sdk-lib-downloader-anagrafiche-client` , `sdk-lib-gestoreanagrafiche-java` , `sdk-lib-components`
- Configurazioni per Validation Engine

---

## ✅ Prerequisiti

### Software richiesto


| Componente     | Versione minima |Note|
|----------------|-----------------|---- |
| Java           |  8 - 11 |   |
| Php            | 8+              | Utilizzato per la creazione di un FE per invocare le API rest tramite Access Layer |
| Maven          | 3.3+            | |
| C#             | 7.3+            | Utilizzato per la creazione di un FE per invocare le API rest tramite Access Layer.| 
| NuGet          | 4.3.0+          | Utilizzato per la creazione di un FE per invocare le API rest tramite Access Layer.| 
| Git            | Ultima stabile  | |


## 1 ️Scarica le dipendenze locali

Le seguenti dipendenze **non sono pubblicate su un repository maven remoto** e vanno costruite localmente:

| ArtifactId                            | Repository GitHub                                                  |
|--------------------------------------|---------------------------------------------------------------------|
| sdk-lib-downloader-anagrafiche-client | https://github.com/ministero-salute/sdk-lib-downloader-anagrafiche-client/ |
| sdk-lib-gestoreanagrafiche-java      | https://github.com/ministero-salute/sdk-lib-gestoreanagrafiche-java/      |
| sdk-lib-components                   | https://github.com/ministero-salute/sdk-lib-components/                  |



Clona quindi nel tuo workspace le librerie richieste:
```bash
cd <Workspace>
git clone https://github.com/ministero-salute/sdk-lib-downloader-anagrafiche-client
git clone https://github.com/ministero-salute/sdk-lib-gestoreanagrafiche-java.git
git clone https://github.com/ministero-salute/sdk-lib-components.git

cd <Workspace>
cd sdk-lib-downloader-anagrafiche-client
mvn clean install

cd <Workspace>
cd sdk-lib-gestoreanagrafiche-java
mvn clean install

cd <Workspace>
cd sdk-lib-components
mvn clean install

```
---

## 2️ Scarica il progetto dell'engine di interesse

Clona il progetto `sdk-engine-<nome_engine>` (esempio `sdk-engine-siarriab-java`) nel tuo workspace:

```bash
cd <Workspace>
git clone https://github.com/ministero-salute/sdk-engine-siarriab-java.git 
cd sdk-engine-siarriab-java
```
---

### 2.1 Installa l'engine di interesse

Dopo aver installato tutte le dipendenze locali, torna nella cartella del progetto `sdk-engine-siarriab-java` e compila:

```bash
cd <Workspace>
cd sdk-engine-siarriab-java
mvn clean install
```

---

## 3 Struttura della cartella `/sdk`
L'engine appena installato avrà bisogno di alcuni file di configurazione che dovranno necessariamente
trovarsi dentro la cartella `sdk` in una posizione ben precisa:
- Windows : `C:\sdk'
- Unix: `\sdk` (root)

Crea quindi la seguente struttura di cartelle nel tuo filesystem locale:

| Nome Cartella   |Note
|----------------|-----------------
| db             | Contiene le anagrafiche di riferimento utilizzate per i controlli dei flussi
| properties             | Contiene le properties specifiche per singolo frusso
| regole             | Contiene i file con le regole specifiche per singolo flusso
| progressivo             | Contiene il file con all'interno il numero progressivo da utilizzare per le run
| xmlinput| Contiene i file xml da validare
| xmloutput             | Conterrà le righe valide della run
| run             | Conterrà i file con l'esito dell'elaborazione in tempo reale
| sent             | Conterrà i file che hanno superato la soglia di invio impostata nelle properties
| esiti             | Conterrà il file con l'esito del run, compresi gli scarti
| log             | Conterrà i log del validation engine

Procedere quindi con la creazione delle cartelle
```bash
cd <Workspace>/sdk
mkdir -p db
mkdir -p sent
mkdir -p esiti
mkdir -p log
mkdir -p progressivo
mkdir -p properties
mkdir -p regole
mkdir -p run
mkdir -p xmlinput
mkdir -p xmloutput

<--nel nostro esempio utilizzeremo siar-riab-->
mkdir -p siar-riab

```

## 5️ Configurazione dei file `.properties`

I seguenti file dovranno essere creati nella cartella `/sdk/properties` (root su Unix) o 'C:/sdk/properties' su windows, gli stessi sono disponibili su richiesta al Ministero della Salute:

### `configurazioni-anagrafiche.properties`
File di properties necessario per il download delle anagrafiche utilizzate per la validazione dei flussi.
```
sqlite.database.file.path=<path completo in cui creare il db sqllite: /sdk/db/anagrafica.db>
resilienza.ore=<time to live delle anagrafiche nel DB in ore: 2>

client.rest.headers.x-pplication-id: APP_ID_REGISTRYDOWNLOADER_CLIENT
client.type=REST
client.host=<url del gestore anagrafi: https://api.salute.gov.it/api/gestanag/v1>

rest.authorizer.type=<tipo del token di autorizzazione/autenticazione: JWT>
rest.authorizer.token-issuer.url=<url per la generazione di un token di autenticazione: https://nsis-ids.sanita.it/nidp/oauth/nam/token>
rest.authorizer.token-issuer.grant_type=<tipo di flow da utilizzare per l'autenticazione: client_credentials>
rest.authorizer.token-issuer.username=<username: verrà fornita dal ministero>
rest.authorizer.token-issuer.password=<password: verrà fornita dal ministero>
rest.authorizer.token-issuer.client_id=<client id: verrà fornita dal ministero>
rest.authorizer.token-issuer.client_secret=<client secret: verrà fornita dal ministero>
rest.authorizer.token-issuer.scope=<scopes a cui deve appartenere l'utenza: verrà fornita dal ministero>
```
### `connettore_mds.properties`
File di properties necessario per la comunicazione con i servizi di invio del file.
```properties
url.webservice.gaf=https://nsis.sanita.it/WSHUBPA/interop_new/services/GAFPDD - URL per l'invio dei flussi al GAF
url.webservice.dpm=https://nsis.sanita.it/WSHUBPA/interop_new/services/PMDAT - URL per l'invio delle DPM
url.webservice.dispovig=https://nsis.sanita.it/WSHUBPA/interop_new/services/DispovigWSUpload - URL per l'invio al servizio DispoVigilance
path.esiti=/sdk/mds/esiti - percorso dove verranno salvati i file scartati dalla validazione 
path.fus=/sdk/SDK_MONITORAGGIO_ELABORAZIONE_MDS/FUS <!--  percorso dove verrà salvato il File Unico Scarti prodotto dal GAF (scarti xsd).
path.esiti.mx11=/sdk/SDK_MONITORAGGIO_ELABORAZIONE_MDS/SCARTI_MDS <!-- percorso dove verrà salvato il file con gli scarti prodotti dall'elaborazione del flusso.
path.form.mds=/sdk/dpm-from-mds
-Credenziali per l'invio dei flussi al GAF
user.invio.mds=<utente>
password.invio.mds=<password>
-Credenziali per l'invio delle DPM
user.invio.mds.dpm=<utente>
password.invio.mds.dpm=<password>
-Credenziali per l'invio al webservice DispoVigilance
user.invio.mds.dispovi=<utente>
password.invio.mds.dispovig=<password>
```
### `File di configurazione specifico per flusso`
Ogni engine ha un suo file di properties, i file sono reperibili facendo specifica richiesta
al Ministero Della Salute.
Il file di properties dovrà essere inserito nel seguente percorso:
`/sdk/properties`.

```
nome.flusso=<nome del flusso lato ministero>
flusso.categoria=<categoria del flusso lato ministero>
flusso.codifica=<codice identificativo del flusso lato ministero>
regole.percorso=<path completo al file xml delle regole: /sdk/regole/regole.xml>

xmloutput.percorso=<path completo della folder in cui scrivere l'output>/SDK_{{periodoRiferimento}}_{{idRun}}.xml (/sdk/xmloutput/SDK_{{periodoRiferimento}}_{{idRun}}.xml)

sent.percorso=<path completo della directory in cui spostare gli xml inviati al ministero: /sdk/sent/>
flusso.percorso=<path completo della directory in cui verranno depositati i file csv: /sdk/dir/>
soglia.invio.mds=<numero intero indicante quanti la soglia di record validi per file affinché possa essere inviato l'xml di output: 100>
separatore=<carattere di separazione dei valori nel csv: ~>

run.percorso=<path completo della directory in storicizzare i file di run: /sdk/run>
esito.percorso=<path completo della directory in cui depositare i file di esito: /sdk/esiti>
progressivo.percorso=<path completo della directory in cui generare il file dat dei progressivi: /sdk/progressivo>

url.rest.gaf=<url per l'invio degli xml: https://api.salute.gov.it/api/gaf/upldfunc>
gaf.sender.authorizer.token-issuer.url=<url per l'autenticazione dell'invio: https://nsis-ids.sanita.it/nidp/oauth/nam/token>
gaf.sender.authorizer.token-issuer.grant_type=<flow di autenticazione: verrà fornita dal ministero>
gaf.sender.authorizer.token-issuer.username=<username: verrà fornita dal ministero>
gaf.sender.authorizer.token-issuer.password=<password: verrà fornita dal ministero>
gaf.sender.authorizer.token-issuer.client_id=<client id: verrà fornita dal ministero>
gaf.sender.authorizer.token-issuer.client_secret=<client secret: verrà fornita dal ministero>
gaf.sender.authorizer.token-issuer.scope=<scopes a cui deve appartenere l'utente: verranno forniti dal ministero>
gaf.sender.authorizer.type=<tipo di token di autenticazione: JWT>
gaf.sender.type=REST
gaf.sender.fileType=<categoria del flusso: valorizzare come flusso.categoria>
```

Nel nostro esempio il file in questione è `config-flusso-siar-riab.properties`, dove andranno modificati solo i placeholder.

## 6 File contenente le regole del singolo flusso
All'interno della cartella `/sdk/regole` andremo ad inserire i file delle regole nel nostro caso `regole-siar-riab.xml` 
I file sono reperibili facendo specifica richiesta al Ministero Della Salute e non dovranno essere modificati.


## 7 Contatore per id_run
Al fine di permettere il corretto funzionamento degli SDK, al prio avvio dell'SDK verrà inizializzato il contatore degli id_run, ovvero un valore intero, contenuto in un file nella cartella 
“/sdk/progressivo”, che rappresenta l’ultimo id_run assegnato ad una esecuzione di un 
Validation Engine. L’id_run è un intero che identifica univocamente un’esecuzione tra tutte le esecuzioni di tutti gli SDK.

NB: nel caso in cui si proceda con un’inizializzazione successiva alla prima (reset del contatore dopo aver eseguito almeno una volta un SDK), sarà necessario svuotare le cartelle “/sdk/run” e “/sdk/esiti”, riportare a 1 anche l'id_run nella cartella progressivo per tutti i file creati.
## 8 File di input

I file di input devono stare sotto la cartella definita nelle properties del singolo flusso nella variabile "flusso.percorso", quando si chiama l'avvio del validation-engine è necessario specificare il "nomeFile" sul quale dovrà essere effettuata la verifica.
Per ciascun flusso, è necessario predisporre uno o più file di input in formato csv e aderenti le specifiche di tracciato.

Di seguito la tabella col mapping tra le cartelle e i relativi flussi e la context-root da chiamare per l'invio ed il monitoraggio.

| Nome Repository |Nome cartella| AREA| Flusso |Context Root|
|-----------------|--------|------------------| -----|-----|
| sdk-engine-dispovigilanza-java | dispovig | DISPOVIG |DISPOVIG| dispovig |
| sdk-engine-prontosoccorso-java | emur-ps  |EMUR | PS1| emur |
| sdk-engine-vaccinianagraficamobilita-java | avn-mobilita-anag |AVT | AVM | vaccinianagraficamobilita |
| sdk-engine-vaccininonsommmobilita-java |avn-non-smom-mob | AVT | VNM | vaccininonsommmobilita |
| sdk-engine-vaccinisommmobilita-java |avn-som-mobilita | AVT | VSM | vaccinisommmobilita |
| sdk-engine-vaccininonsommresidenti-java |avn-non-som-res | AVN | VNX | vaccininonsommresidenti |
| sdk-engine-vaccinianagraficaresidenti-java | avn-reg-anag |AVN | AVX | vaccinianagraficaresidenti |
| sdk-engine-vaccinisommresidenti-java |avnsom-res | AVN | VSX | vaccinisommresidenti |
| sdk-engine-diretta-java |dir | DIR | IF3 | diretta |
| sdk-engine-donazionipostmortem-java |dpm | DPM | DPM| donazionipostmortem |
| sdk-engine-sismresidenzialeprestazioni-java |sism-res | RES | PSR | sismresidenzialeprestazioni |
| sdk-engine-sismresidenzialeanagrafica-java |sism-reg-anag | RES | ANR | sismresidenzialeanagrafica |
| sdk-engine-sismresidenzialecontatti-java |sism-reg-cont | RES | CNR | sismterritorialeprestazioni |
| sdk-engine-sismsemiresidenzialeprestazioni-java |sism-semires | TER | PSS | sismsemiresidenzialeprestazioni |
| sdk-engine-sismterritorialeprestazioni-java |sism-ter | TER|  PST | sismterritorialeprestazioni |
| sdk-engine-sismterritorialeanagrafica-java |sism-ter-anag | TER | ANT | sismterritorialeanagrafica |
| sdk-engine-sismterritorialecontatti-java |sism-ter-cont | TER | CNT | sismterritorialecontatti |
| sdk-engine-siarpicvalini-java|siar-picvalini|SIAR|picvalini| SIAR_PICVALINI |
| sdk-engine-siarreprof-java|siar-reprof|SIAR|reprof| SIAR_OREPROF |
| sdk-engine-siarriab-java|siar-riab|SIAR|riab| SIAR_RIAB |
| sdk-engine-sicofprestind-java|sicof-prestind|SICOF|prestind| SICOF_PRESTIND |
| sdk-engine-sicofcontatto-java|sicof-contatto|SICOF|contatto| SICOF_CONTATTO |
| sdk-engine-sicofprestgrup-java|sicof-prestgrup|SICOF|prestgrup| SICOF_PRESTGRUP |


## 9 Avvio Validation Engine
Una volta configurate le properties del validation engine è necessario lanciare il jar creato al punto 2.1 , il jar sarà stato salvato nella cartella "target", è possibile copiare il jar in un qualsiasi percorso <jar_sdk>.
Il nome del jar da eseguire dovrà essere lo stesso del jar generato al punto 2.1 , è possibile specificare la porta sul quale il validation engine sarà in ascolto, ciò consentirà di eseguire più validation engine sulla stessa macchina esponendo i servizi su porte differenti (es. DIR porta 8080, PS1 porta 8082, non è possibile esporre più servizi sulla stessa macchina e stessa porta).
Per il corretto avvio è necessario specificare nella variabile "--config="

```bash
cd <Workspace>

java -jar -Dserver.port=9091 "C:\sdk\sdk-engine-siarriab-java-1.0.0-SNAPSHOT.jar" --config="C:/sdk/properties/config-flusso-siar-riab.properties"

```
### `Test Validation Engine`
E' possibile testare il corretto funzionamento del validation engine tramite PostMan o Curl
### `API Validation Engine per l’invio dei dati`
L’operation in oggetto consente di validare ed eventualmente inviare un file al sistema GAF; 

HTTP Method: POST

URL: http:[HOST]:[PORTA]/v1/flusso/[CONTEXT ROOT]

Dove:
```bash
curl 'http:[HOST]:[PORTA]/v1/flusso/[CONTEXT ROOT]' \
--header 'Content-Type: application/json' \
--header 'Authorization: ••••••' \
--data '{
    "nomeFile": "SIAR_riab.csv",
    "idClient": "1663" ,
    "modalitaOperativa":"P",
    "annoRiferimento": "2022",
    "periodoRiferimento": "13",
    "codiceRegione": "120"
}
```
**• Host e porta**: sono configurati in fase di avvio Validation Engine;

**• Context root**: identifica il particolare flusso che si vuole eseguire. In Allegato A1 è presente una tabella con tutti i valori. 
i Parametri del Body JSON:
1. **Id Client**: Stringa Numerica rappresentante un l’identificativo univoco della trasazione che fa richiesta all' SDK. I possibili valori sono a discrezione del chiamante.
2. **Nome File**: Stringa rappresentante il nome del file presente al path predisposto per il 
flusso da utilizzare. Di seguito la coppia flusso-path sotto cui inserire i file di input:
	a. AVN-AVM: /sdk/avn-mobilita-anag
	b. AVN-VNM: /sdk/avn-non-som-mob
	c. AVN-VNX: /sdk/avn-non-som-res
	d. AVN-AVX: /sdk/avn-res-anag
	e. AVN-VSM: /sdk/avn-som-mobilita
	f. AVN-VSR: /sdk/avn-som-res
	g. SALM -PNR: /sdk/salm-pnr
	h. SALM-PSD: /sdk/salm-psd
	i. SALM-VIG: /sdk/salm-vig
	j. SISM-ANR: /sdk/sism-res-anag
	k. SISM-ANT /sdk/sism-ter-anag
	l. SISM-CNT: /sdk/sism-ter-cont
	m. SISM-CNR: /sdk/sism-res-cont
	n. SISM-PSS: /sdk/sism-semires
	o. SISM-PSR: /sdk/sism-res
	p. SISM-PST: /sdk/sism-ter
	q. DIRETTA: /sdk/dir
	r. EMURPS: /sdk/emurps
	s. DISPOVIG: /sdk/dispovig
	t. DPM: /sdk/dpm
3. **Anno riferimento**: Stringa numerica rappresentante l’anno di riferimento per cui si 
intende inviare il flusso
4. M**odalità Operativa**: Stringa rappresentante la modalità di utilizzo 
dell’SDK(Produzione: “P”/Test:”T”)
5. **Codice Regione**: Stringa Numerica rappresentante la Regione Mittente.

### `Informazioni della risposta:`
Di seguito un esempio:
```bash
{
 "descrizioneErrore": "",
 "idClient": "7890",
 "isIniziato": true,
 "idrun": "6983"
}
```

1. **descrizioneErrore**: Stringa rappresentante la descrizione dell’errore della presa in 
carico dell’elaborazione da parte dell’SDK
2. **idClient**: Id Client passato in input
3. **isIniziato**: Booleano rappresentate la presa in carico dell’elaborazione
4. **idrun**: Id Run generato dall’SDK da utilizzare per l’API per il monitoraggio

### `Monitoraggio`
L’operation in oggetto consente, a partire da Id Run e Id Client di monitorare lo stato d’esecuzione del Validation;

**HTTP Method**: GET

**URL**: 
http:[HOST]:[PORTA]/v1/flusso/[CONTEXT_ROOT]/info?idClient=[ID_CLIENT]&idRun
=[ID_RUN]
```bash
curl 'http:[HOST]:[PORTA]/v1/flusso/[CONTEXT_ROOT]/info?idClient=[ID_CLIENT]&idRun=[ID_RUN]' \
--header 'Content-Type: application/json' \
--header 'Authorization: ••••••' \
--data '{
    "idRun": "6983",
    "idClient": "7890" 
}
```
Dove:

• **Host e porta** sono configurati in fase di avvio del Validation 
Engine;

• **Context root**: identifica il particolare flusso per il quale si vuole richiedere il 
monitoraggio dell’esecuzione.

• **idClient**: intero rappresentante l’identificativo univoco della transazione passato 
in input, in una precedentemente chiamata all’API per l’invio dei dati.

• **idRun**: intero rappresentante l’identificativo univoco dell’esecuzione dell’SDK 
generato precedentemente dall’API per l’invio dei dati
### `Informazioni della risposta:`
**1. Id Run**: Id Run (Stringa) ottenuta dalla risposta dell’API per l’invio dei dati 

**2. Id Client**: Id Client (Stringa) ottenuta dalla risposta dell’API per l’invio dei dati

**3. Id Uploads**: Array contenente un sola Stringa rappresentante l’identificativo di 
caricamento fornito da MdS

**4. Tipo Elaborazione**: Stringa rappresentante la tipologia di elaborazione: F (full)/R 
(per singolo record) - Impostato di default a F

**5. Mod Operativa**: Stringa rappresentante la modalità operativa: P (=produzione) /T 
(=test)

**6. Data Inizio Esecuzione**: Stringa rappresentante il Timestamp dell’ inizio del 
processamento

**7. Data Fine Eesecuzione**: Stringa rappresentante il Timestamp di completamento del 
processamento

**8. Stato Esecuzione**: Stringa rappresentate lo stato d’esecuzione 

**9. Descrizione Stato Esecuzione**: Stringa rappresentate la descrizione dello stato 
d’esecuzione

**10. Nome Flusso**: Stringa rappresentante il valore fisso che identifica lo specifico SDK in 
termini di categoria e nome flusso

**11. Numero Record**: Intero rappresentante il numero di record del flusso input

**12. Numero Record Accettati**: Intero rappresentante il numero di record validi

**13. Numero Record Scartati**: Intero rappresentante il numero scarti

**14. Version**: Stringa rappresentante la versione dell’ SDK (Access Layer e Validation 
Engine)

**15. Timestamp Creazione**: Stringa rappresentante il Timestamp creazione della info run

**16. API (*DPM)**: Stringa rappresentante l’API utilizzata per il flusso DPM (non 
valorizzata per gli altri flussi)

**17. Identificativo Soggetto Alimentante (*DPM)**: Stringa rappresentante l’analogo 
campo del tracciato di input del flusso DPM (non valorizzata per gli altri flussi) 

**18. Tipo Atto (*DPM)**: Stringa rappresentante l’analogo campo del tracciato di input del 
flusso DPM (non valorizzata per gli altri flussi)

**19. Numero Atto(*DPM)**: Stringa rappresentante l’analogo campo del tracciato di input 
del flusso DPM (non valorizzata per gli altri flussi)

**20. Tipo Esito MDS (*DPM)**: Stringa rappresentante l’esito della response dell’API 2 
(non valorizzata per gli altri flussi) 

**21. Data Ricevuta MDS (*DPM)**: Stringa rappresentante la data della response dell’API 
3 (non valorizzata per gli altri flussi)

**22. Codice Regione**: Stringa rappresentante il Codice Regione del Mittente

**23. Anno Riferimento**: Stringa rappresentante l’Anno passato all’API per l’invio del dei dati

**24. Periodo di riferimento**: Stringa rappresentante il periodo di riferimento passato 
all’API per l’invio del dei dati

**25. Nome File Output**: Stringa il nome del file di output generato ed eventualmente 
inviato a MdS presente sotto la cartella /sdk/xmloutput

**26. Esito acquisizione flusso**: Stringa rappresentante il codice dell’esito del processo di 
acquisizione del flusso su MdS. 

**27. Codice errore invio flussi**: Stringa rappresentante il codice d’errore della procedura 
di invio. 

**28. Testo errore invio flussi**: Stringa rappresentante la descrizione del codice d’errore 
della procedura. 

**29. File Associati Run**: Stringa rappresentante il nome del file di input elaborato 
dall’SDK e passato in input all’API per l’invio del flusso

Di seguito un esempio:
``` bash
{
    "idRun": "4",
    "idClient": "1663",
    "idUploads": null,
    "tipoElaborazione": "F",
    "modOperativa": "P",
    "dataInizioEsecuzione": 1753791785948,
    "dataFineEsecuzione": 1753791785984,
    "statoEsecuzione": "KO_GENERICO",
    "descrizioneStatoEsecuzione": "\\sdk\\flusso_siar\\SIAR_riab.csv (The system cannot find the path specified)",
    "fileAssociatiRun": "SIAR_riab.csv",
    "nomeFlusso": "SIAR_RIAB",
    "numeroRecord": 0,
    "numeroRecordAccettati": 0,
    "numeroRecordScartati": 0,
    "version": "1.0.0-SNAPSHOT",
    "timestampCreazione": 1753791785916,
    "api": null,
    "identificativoSoggettoAlimentante": null,
    "tipoAtto": null,
    "numeroAtto": null,
    "tipoEsitoMds": null,
    "dataRicevutaMds": null,
    "codiceRegione": "120",
    "annoRiferimento": "2022",
    "periodoRiferimento": "13",
    "nomeFileOutputMds": null,
    "esitoAcquisizioneFlusso": null,
    "codiceErroreInvioFlusso": null,
    "testoErroreInvioFlusso": null
}
```

---

## 📌 Note Finali

- È consigliato conservare un log dettagliato nella cartella `/sdk/log`.
- È consigliato modificare solamente i placeholder nei file di properties.
- Sul GitHub sono presenti gli Access Layer sviluppati in linguaggio C#, PHP o Java, che permettono di integrare all’interno di un applicativo preesistente il meccanismo di invocazione del Validation Engine (o del “SDK di Ritorno”)

## 📞 Contatti

Per ulteriori informazioni, contattare:

- **Service Desk - Ministero della Salute**: servicedesk.mds@medilifegroupspa.com
- **Amministrazione titolare**: [Ministero della Salute](https://www.salute.gov.it)

---
