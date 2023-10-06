<template>
  <ResultBoardTemplate :loser="loser" :b-id="bId.toString()" :user-id="id" :a-id="aId.toString()" @restart="restart"/>
</template>

<script setup lang="ts">
import ResultBoardTemplate from "@/components/pk/ResultBoardTemplate.vue";
import userUseStore from '@/stores/user'
import gobangPkUseStore from '@/stores/gobang/pk'
import {storeToRefs} from "pinia";
import image from "@/enums/Image";

const userStore = userUseStore()
const gobangStore = gobangPkUseStore()

const {id} = storeToRefs(userStore)
const {aId, bId, loser} = storeToRefs(gobangStore)
const {updateLoser, updateOpponent, updateStatus} = gobangStore
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