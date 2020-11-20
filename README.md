# "Share Or Take?"

**Disciplina**: FGA0210 - PARADIGMAS DE PROGRAMAÇÃO - T01  
**Nro do Grupo**: 05  
**Paradigma**: SMA

## Alunos

|Matrícula | Aluno |
| -- | -- |
| 16/0049458 | Eduardo Lima Ribeiro |
| 17/0070735  |  Lucas Maciel Aguiar |

## Sobre

Esse projeto consiste em uma simulação simples para demonstrar a aplicabilidade de Sistemas MultiAgentes em uma simulação de sobrevivência de dois tipos de espécies ficiticias de criaturas, os Doves que são passíficos e os Hawks que são agressivos. As duas espécies compartilham  os mesmos recursos, porém cada espécie possui sua própria estratégia de sobrevivência.  
Essa idéia de simulação foi baseado no vídeo da primeira referência listada nesse projeto.

### Interação entre as Espécies

As espécies possuem comportamentos bem simples baseados. Abaixo está listado as interações possíveis nesse ambiente:

* Quando Doves ou Hawks tomam posse de um recurso disponível no mapa, eles conseguem sobreviver por mais um dia e ainda tem 100% de chance de se reproduzir durante a noite;  
* Quando um Dove encontra outro Dove, eles compartilham o alimento, já que cada fonte de alimento possui duas unidades, então os dois voltam para seu ponto inicial com 100% de śobreviver, porém dessa vez eles não possuem chance de se reproduzir durante a rodada;  
* Quando um Dove encontra um Hawk, o Hawk não aceita compartilhar o alimento e os dois partem para a briga, como o Hawk é mais sagaz, ele sai da briga com 100% de chance de sobreviver e 50% de chance de reproduzir. Por outro lado o Dove apenas tem 50% de chance de sobreviver;  
* O último caso é quando um Hawk encontra outro Hawk. Os dois por sua vez saem da briga com 0% de chance de sobreviverem.

### Ambiente

O sistema do ambiente é baseado em turnos, quando está de dia os Doves e Hawks saem para caçar comida e quando anoitece eles voltam para casa.

## Screenshots

![screen1](resources/screen1.png)
![screen2](resources/screen2.png)

## Instalação

**Linguagens**: Java  
**Tecnologias**: JADE, JFrame  

Descreva os pré-requisitos para rodar o seu projeto e os comandos necessários.
Insira um manual ou um script para auxiliar ainda mais.

## Uso

Certifique-se que tenha JRE 11, Eclipse ou um IDE Java equivalente, JADE e JFrame instalados.
![screen3](resources/screen3.png)
Defina a classe principal como jade.Boot.

![screen4](resources/screen4.png)
Adicione o argumento

    -gui environment:simulation.environment.EnvironmentAgent

Certifique-se de que JRE 11 esteja selecionado para o projeto.
![screen5](resources/screen5.png)

Configure a simulação como desejar, e clique em iniciar!
![screen6](resources/screen6.png)

## Vídeo

[video de execução](https://youtu.be/pDlHvPBiERk)

## Fontes

__Simulação de compartilhamento de recursos em uma comunidade heterogênea:__ <https://www.youtube.com/watch?v=YNMkADpvO4w&t=685s&ab_channel=Primer>  
__Exemplo de Party JADE__: <https://jade.tilab.com/documentation/examples/party/>