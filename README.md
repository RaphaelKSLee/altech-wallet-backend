# Altech Wallet Backend
Take home assignment from Altech.
- partially an assignment
- partially for fun / an exercise for:
  - Spring Modulith
  - DDD
  - Clean architecture

## Deployment

### Prerequisites
Docker
### Build docker image
```shell
./mvnw package
```

### Deploy
```shell
docker compose up
```



## Design decisions

### Clean architecture
![components](docs/diagrams/components.svg)


## Assumptions / limitations
- 1-1 Player-Wallet(USD) mapping
  - Minimize engineering time
- No simple credit / debit support in application layer
  - Must be transaction with clear to/from accounts
- 19 significant digit transactions
