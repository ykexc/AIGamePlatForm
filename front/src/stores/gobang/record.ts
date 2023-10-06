import {defineStore} from "pinia";

interface RecordAttribute {
    is_record: boolean
    a_steps: Array<Array<number>> | null
    b_steps: Array<Array<number>> | null
    record_loser: string
    is_running: boolean
    a_username: string
    b_username: string
}

interface OneRound {
    a_steps: Array<Array<number>>
    b_steps: Array<Array<number>>
}

export default defineStore('gobang_record', {
    state: (): RecordAttribute => {
        return {
            is_record: false,
            a_steps: null,
            b_steps: null,
            record_loser: "",
            is_running: false,
            a_username: '',
            b_username: ''
        }
    },
    actions: {
        updateGoBangIsRecord(is_record: boolean) {
            this.is_record = is_record
        },
        updateGoBangSteps(data: OneRound) {
            this.a_steps = data.a_steps
            this.b_steps = data.b_steps
        },
        updateGoBangRecordLoser(record_loser: string) {
            this.record_loser = record_loser
        },
        updateIsRunning(isRunning: boolean) {
            this.is_running = isRunning
        },
        updatePlayer(aUsername: string, bUsername: string) {
            this.a_username = aUsername
            this.b_username = bUsername
        }
    }
})