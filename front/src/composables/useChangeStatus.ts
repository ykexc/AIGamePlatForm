import {readonly, ref, type Ref} from "vue";


interface ChangeStatus {
    str: Ref<string>
    change: (val: string) => void
}

export function useChangeStatus(s: string): ChangeStatus {
    const str: Ref<string> = ref(s)
    const change = (val: string): void => {
        str.value = val
    }
    return {
        str: readonly(str), change
    }
}