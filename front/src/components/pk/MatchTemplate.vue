<template>
  <div class="match-ground">
    <el-row>
      <el-col :span="8">
        <div class="user-photo">
          <img :src="myImg" alt="">
        </div>
        <div class="user-username">
          {{ myName }}
        </div>
      </el-col>

      <el-col :span="8">
        <div class="user-select-bot">
          <el-select
              v-model="selectValue"
              placeholder="亲自出马"
              loading-text="正在加载你的bot"
          >
            <el-option
                value='-1'
                label="亲自出马"
            />
            <el-option
                v-for="item in bots"
                :key="item.title"
                :label="item.title"
                :value="item.id"
            />
          </el-select>
        </div>
      </el-col>

      <el-col :span="8">
        <div class="user-photo">
          <img :src="rivalImg" alt="">
        </div>
        <div class="user-username">
          {{ rivalName }}
        </div>
      </el-col>

      <el-col style="text-align: center;padding-top: 15vh">
        <el-button @click="actionDo" type="warning" round size="large">{{ props.str }}</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import userUseStore from '@/stores/user'
import {computed, onMounted, ref} from "vue";
import {get} from "@/util/request";


interface Bot {
  id: number
  userId: number
  title: string
  description: string
  content: string
  type: string
  rating: number
  createTime: string
  modifyTime: string
  game: string
}

interface Prop {
  str: string
  loading: boolean
  rivalImg: string
  rivalName: string
  url: string

}

const bots = ref<Array<Bot>>()
onMounted(() => {
  get({
    url: props.url,
    isAuth: true,
    success: (res) => {
      bots.value = res
    }
  })
})

const userStore = userUseStore()
const myImg = computed(() => {
  return userStore.photo
})
const myName = computed(() => {
  return userStore.username
})


const props = withDefaults(defineProps<Prop>(), {
  str: '开始匹配',
  loading: true
})


const emit = defineEmits<{
  (e: 'action', v: number | undefined | string): void
}>()


const selectValue = ref<string>('-1')


const actionDo = () => {
  emit('action', selectValue.value)
}
</script>

<style scoped>
div.match-ground {
  width: 60vw;
  height: 70vh;
  margin: 40px auto;
  background: rgba(50, 50, 50, 0.5);
}

div.user-photo {
  text-align: center;
  padding-top: 10vh;
}

div.user-photo > img {
  border-radius: 50%;
  width: 20vh;
}

div.user-username {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  color: aliceblue;
  padding-top: 2vh;
}

div.user-select-bot {
  text-align: center;
  padding-top: 20vh;
}

div.user.user-select-bot > select {
  width: 60%;
  margin: 0 auto;
}
</style>