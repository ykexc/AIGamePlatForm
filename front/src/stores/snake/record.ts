import {defineStore} from "pinia";

interface RecordAttribute {
    is_record: boolean
    a_steps: string
    b_steps: string
    record_loser: string
}

interface OneRound {
    a_steps: string
    b_steps: string
}

export default defineStore('snake_record', {
    state: (): RecordAttribute => {
        return {
            is_record: false,
            a_steps: "",
            b_steps: "",
            record_loser: ""
        }
    },
    actions: {
        updateIsRecord(is_record: boolean) {
            this.is_record = is_record
        },
        updateSteps(data: OneRound) {
            this.a_steps = data.a_steps
            this.b_steps = data.b_steps
        },
        updateRecordLoser(record_loser: string) {
            this.record_loser = record_loser
        }
    }
})