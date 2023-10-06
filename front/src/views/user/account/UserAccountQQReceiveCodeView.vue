<template>
  <div></div>
</template>


<script setup lang="ts">
import {useRoute} from "vue-router";
import userUseStore from '@/stores/user'
import router from "@/router";
import {onMounted, ref} from "vue";
import get from "axios";
import type {AxiosError} from 'axios'


onMounted(() => {

  const userStore = userUseStore()
  const route = useRoute()
  const {UpdateToken, UpdateState} = userStore

  const code = ref('')
  const state = ref('')
  if (route.query.code && route.query.state) {
    code.value = route.query.code.toString()
    state.value = route.query.state.toString()
  }

  console.log(code.value)
  console.log(state.value)

  get(
      '/api/user/account/qq/receive_code/',
      {
        params: {
          code: code.value,
          state: state.value
        }
      }
  ).then((resp: any) => {
    console.log(resp)
    if (resp.data.result === 'success') {
      console.log(resp)
      localStorage.setItem("jwt_token", resp.data.jwt_token);
      UpdateToken(resp.data.jwt_token);
      UpdateState(true)
      router.push('/');
    } else {
      console.log('failed')
      router.push('/user/account/login');
    }
  }).catch((_e: AxiosError | Error) => {
    console.log('error')
    router.push('/user/account/login');
  })

})

</script>

<style scoped>

</style>