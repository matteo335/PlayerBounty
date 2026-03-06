# Eventos:

## Player ao eliminar outro Player:
- Ganha pontos de Bounty ao eliminar outro Player.
- Perde pontos de Bounty ao ser eliminado por outro Player.
- Ganha um percentual das moedas ao eliminar outro Player.
- Perde um percentual das moedas ao ser eliminado por outro Player

## Pontos de Bounty
Representam o nível de recompensa e risco associado a cada jogador, sendo diretamente influenciados por confrontos entre Players.

## Regras de ganho de pontos de Bounty

- Formatos de jogo:
    1. **Caçador de Recompensas**: Ao eliminar jogadores ganhará pontos de Bounty e recompensas seguindo as
    seguintes regras.
        - Jogadores com **Pontos de Bounty** maior que zero ao eliminar jogadores com **Pontos de Bounty** igual a zero não recebem aumento nos **Pontos de Bounty**.
        - Jogadores com **Pontos de Bounty** igual a zero ao eliminar jogadores com **Pontos de Bounty** igual a zero, recebem a taxa base.
        - Jogadores com **Pontos de Bounty** maior que zero ao eliminar jogadores com **Pontos de Bounty** maior que zero recebem a taxa base mais o percentual com base na quantidade de **Pontos de Bounty** do jogador eliminado. Essa porcentagem varia com base no arquivo de configuração.

    2. **Caçador de Fortunas**: Ao eliminar jogadores ganhará saldo/moedas do jogador eliminado, porém deve seguir algumas regras.
        - A recompensa recebida de saldo/moedas ao eliminar um jogador pode ser no percentual padrão do SG-Economy API ou Customizado no arquivo de configuração.
        - Jogadores sem saldo/moedas não possuem recompensa.
        - Os **Pontos Bounty** neste modo os **Pontos de Bounty** são baseados com base na quantidade de saldo/moedas que o jogador 
        possuí guardado em sua bolsa.

    3. **Jogo da Ganancia**:
        Possuí todas as regras anteriores dos outros modos, porém com alguns ajustes.
        - Os **Pontos de Bounty** são baseados em 2 métricas quantidade de vezes que aquele jogador eliminou outro jogador mais a quantidade de saldo/moedas que aquele jogador possuí. 

- Variaveis de configuração:
    
    Modo de jogo: "bounty_hunter", "fortune_hunter" e "greed_game".

    **Caçador de Recompensas**:
    - Taxa Base
    - Percentual

    **Caçador de Fortunas**:
    - Taxa Base
    - Ativado Percentual Customizado
    - Percentual

