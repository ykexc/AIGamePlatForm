import {defineStore} from "pinia";
import type {GameMap} from "@/assets/scripts/gobang/GameMap";


interface PkAttribute {
    round: string
    status: string
    aId: number
    bId: number
    socket: null | WebSocket
    gamemap: Array<Array<number>> | null
    aBotId: number | null
    bBotId: number | null
    opponent_username: string
    opponent_photo: string
    gameObject: GameMap | null
    loser: string
    compile: boolean

}

interface Game {
    aId: number
    bId: number
    gamemap: Array<Array<number>>
}

interface Opponent {
    opponent_username: string
    opponent_photo: string
}

export default defineStore('gobang_pk', {
    state: (): PkAttribute => {
        return {
            round: 'none',
            status: "matching",
            aId: 0,
            bId: 0,
            socket: null,
            gamemap: null,
            aBotId: null,
            bBotId: null,
            opponent_username: '',
            opponent_photo: '',
            gameObject: null,
            loser: 'none',
            compile: false
        }
    },

    actions: {

        updateStatus(status: string, aBotId: number | null, bBotId: number | null) {
            this.status = status
            this.aBotId = aBotId
            this.bBotId = bBotId
        },

        updateRound(round: string) {
            this.round = round
        },

        updateOpponent(opponent: Opponent) {
            this.opponent_username = opponent.opponent_username
            this.opponent_photo = opponent.opponent_photo
        },

        updateSocket(socket: WebSocket) {
            this.socket = socket
        },
        updateGobangGame(game: Game) {
            this.gamemap = game.gamemap
            this.aId = game.aId
            this.bId = game.bId
        },
        updateGameObject(gameObject: GameMap) {
            this.gameObject = gameObject
        },
        updateLoser(loser: string) {
            this.loser = loser
        },
        updateCompile(flag: boolean) {
            this.compile = flag
        }
    }
})