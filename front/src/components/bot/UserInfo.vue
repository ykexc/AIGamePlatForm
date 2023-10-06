<template>
    <el-card>
        <el-image :src="img" />
    </el-card>
</template>


<script setup lang="ts">
import userUseStore from '@/stores/user'
import {computed} from "vue";

const store = userUseStore()

const img = computed(() => {
    return store.photo
})




</script>


<style scoped>

</style>