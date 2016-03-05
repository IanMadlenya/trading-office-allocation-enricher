# Trading Office [![Build Status](https://travis-ci.org/spolnik/trading-office.svg?branch=master)](https://travis-ci.org/spolnik/trading-office) [![codecov.io](https://codecov.io/github/spolnik/trading-office/coverage.svg?branch=master)](https://codecov.io/github/spolnik/trading-office?branch=master) [![Sonar Coverage](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/coverage.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Sonar Tech Debt](https://img.shields.io/sonar/https/sonar-nprogramming.rhcloud.com/trading-office/tech_debt.svg)](https://sonar-nprogramming.rhcloud.com/dashboard/index/1) [![Coverity Scan Build Status](https://scan.coverity.com/projects/7604/badge.svg)](https://scan.coverity.com/projects/spolnik-trading-office)

Trading Office is reference implementation of microservices architecture, based on Spring Boot. It's modeling part of post trade processing, mainly focused on receiving Fixml message and preparing confirmation for it.

- [Trading Office](#trading-office)
- [Allocation Enricher](#allocation-enricher)
- [Notes](#notes)

## Trading Office

- [Trading Office](https://github.com/spolnik/trading-office)

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/component_diagram.png)

## Allocation Enricher
- spring boot application
- subscribes to messaging queue looking for allocation messages (json)
- after receiving allocation, it enriches it with instrument and counterparty data
- finally, it sends enriched allocation as json into destination message queue

Heroku: http://allocation-enricher.herokuapp.com/health

![Component Diagram](https://raw.githubusercontent.com/spolnik/trading-office/master/design/allocation_enricher.png)

## Notes
- checking if [dependencies are up to date](https://www.versioneye.com/user/projects/56ad39427e03c7003ba41427)
- installing RabbitMQ locally (to run end to end test locally) - [instructions](https://www.rabbitmq.com/download.html)
- to run on Mac OS X - /usr/local/sbin/rabbitmq-server 
