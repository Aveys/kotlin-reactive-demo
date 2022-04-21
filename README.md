# kotlin-reactuve-demo

## Producer 

Faire un producer ne kotlin coroutines ?
Sinon faire un script en typescript

## back
2 endpoint : 
* PUT /{idStation}/points --> Sauvegarde un point méteo pour une station donnée
* GET /{idStation}/points --> Récupère tout les points de la station
* GET /allPoint --> Recupere tout les point de toutes les stations

MongoDB sur Docker / 

### Modele BDD

collection points
```JSON
{
  "temp": "Double",
  "unit": "Char",
  "wind": "String"
}
```

collection station 
```JSON
{
  "id": "String",
  "name": "String",
  "location": "String" 
}
```

### Step: 
1. webflux (NDJSON)
2. Coroutine (SSE)
3. RSocket

## Front

React (ça va plus vite) : 

RxJS
SSE
RSocket


