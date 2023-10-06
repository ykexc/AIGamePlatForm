const AC_GAME_OBJECTS: any = [];

export class AcGameObject {

    public timedelta: number
    public has_called_start: boolean

    constructor() {
        AC_GAME_OBJECTS.push(this);
        this.timedelta = 0;
        this.has_called_start = false;
    }

    start(): void {  // 只执行一次

    }

    update(): void {  // 每一帧执行一次，除了第一帧之外

    }

    on_destroy(): void {  // 删除之前执行

    }

    destroy(): void {
        this.on_destroy();
        for (let i = 0; i < AC_GAME_OBJECTS.length; i++) {
            const obj = AC_GAME_OBJECTS[i];
            if (obj === this) {
                AC_GAME_OBJECTS.splice(i, 1);
                break;
            }
        }
    }
}

let last_timestamp: number // 上一次执行的时刻
const step = (timestamp: number): void => {
    for (const obj of AC_GAME_OBJECTS) {
        if (!obj.has_called_start) {
            obj.has_called_start = true;
            obj.start();
        } else {
            obj.timedelta = timestamp - last_timestamp;
            obj.update();
        }
    }

    last_timestamp = timestamp;
    requestAnimationFrame(step)
}

requestAnimationFrame(step)
