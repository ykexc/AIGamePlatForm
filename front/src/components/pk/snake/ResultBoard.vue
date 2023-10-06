<template>
  <ResultBoardTemplate :loser="loser" :b-id="b_id.toString()" :user-id="id" :a-id="a_id.toString()" @restart="restart"/>

</template>


<script setup lang="ts">
import pkUseStore from '@/stores/snake/pk'
import userUseStore from '@/stores/user'
import {storeToRefs} from "pinia";
import image from "@/enums/Image";
import ResultBoardTemplate from "@/components/pk/ResultBoardTemplate.vue";

const pkStore = pkUseStore()
const userStore = userUseStore()
const {updateOpponent, updateLoser, updateStatus} = pkStore
const {id} = storeToRefs(userStore)
const {loser, a_id, b_id} = storeToRefs(pkStore)
const restart = () => {
  updateOpponent({
    opponent_username: "我的对手",
    opponent_photo: image.rival
  })
  updateLoser('none')
  updateStatus('matching', null, null)
}
</script>


<style scoped>
</style>