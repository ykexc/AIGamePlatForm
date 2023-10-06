<template>
  <NavaBar/>
    <div class="common-layout">
        <el-container>
            <el-main>
                <el-row>
                    <el-col>
                        <el-card shadow="never" style="background-color: snow">
                                <el-row :gutter="20">
                                    <el-col :sm="8" :md="6">
                                        <UserInfo/>
                                    </el-col>
                                    <el-col :sm="16" :md="18">
                                        <BotInfo/>
                                    </el-col>
                                </el-row>
                        </el-card>
                    </el-col>
                </el-row>
            </el-main>
        </el-container>
    </div>


</template>

<script lang="ts" setup>
import UserInfo from "@/components/bot/UserInfo.vue";
import BotInfo from "@/components/bot/BotInfo.vue";
import NavaBar from "@/components/NavaBar.vue";


</script>

<style scoped>

.common-layout {
    margin-left: 150px;
    margin-right: 150px;
}



</style>