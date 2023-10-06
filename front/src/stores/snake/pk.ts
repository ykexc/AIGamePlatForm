import {defineStore} from 'pinia'
import type {GameMap} from "@/assets/scripts/snake/GameMap";


interface PkAttribute {
    status: string
    socket: null | WebSocket
    opponent_username: string
    opponent_photo: string
    gamemap: Array<Array<number>> | null
    aBotId: number | null
    bBotId: number | null
    compile: boolean
    a_id: number
    a_sx: number
    a_sy: number
    b_id: number
    b_sx: number
    b_sy: number
    gameObject: GameMap | null
    loser: string
}

interface Opponent {
    opponent_username: string
    opponent_photo: string
}

interface Game {
    map: Array<Array<number>>
    a_id: number
    a_sx: number
    a_sy: number
    b_id: number
    b_sx: number
    b_sy: number
}

export default defineStore("snake_pk", {
    state: (): PkAttribute => {
        return {
            status: "matching",
            socket: null,
            opponent_username: "",
            opponent_photo: "",
            gamemap: null,
            aBotId: null,
            bBotId: null,
            compile: false,
            a_id: 0,
            a_sx: 0,
            a_sy: 0,
            b_id: 0,
            b_sx: 0,
            b_sy: 0,
            gameObject: null,
            loser: "none",//none表示在对局中
        }
    },
    actions: {
        updateSocket(socket: WebSocket) {
            this.socket = socket
        },
        updateOpponent(opponent: Opponent) {
            this.opponent_photo = opponent.opponent_photo
            this.opponent_username = opponent.opponent_username
        },
        updateStatus(status: string, aBotId: number | null, bBotId: number | null) {
            this.status = status
            this.aBotId = aBotId
            this.bBotId = bBotId
        },
        updateGame(game: Game) {
            this.gamemap = game.map
            this.a_id = game.a_id
            this.a_sy = game.a_sy
            this.a_sx = game.a_sx
            this.b_sx = game.b_sx
            this.b_id = game.b_id
            this.b_sy = game.b_sy
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