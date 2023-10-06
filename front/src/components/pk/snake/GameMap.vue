<template>
  <div ref="parent" class="gamemap">
      <canvas ref="canvas" tabindex="0"></canvas>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from "vue";
import pkUseStore from '@/stores/snake/pk'
import recordUseStore from "@/stores/snake/record";
import {GameMap} from "@/assets/scripts/snake/GameMap";
const pkStore = pkUseStore()
const recordStore = recordUseStore()
const {updateGameObject} = pkStore
const parent = ref(), canvas = ref()
onMounted(() => {
    updateGameObject(new GameMap(canvas.value.getContext('2d'), parent.value, pkStore, recordStore))
})
</script>

<style scoped>
.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>