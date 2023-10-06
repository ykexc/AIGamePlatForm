<template>
  <NavaBar/>
    <div class="card-container">
        <el-card class="box-card" shadow="always">
            <template #header>
                <div class="card-header">
                    <span class="gradient-text">天梯排行榜</span>
                </div>
            </template>
            <el-table :data="users" table-layout="fixed" style="width: 100%">
                <el-table-column label="玩家">
                    <template #default="scope">
                        <div style="display: flex; align-items: center">
                            <el-avatar :src="scope.row.photo"/>
                            <span>{{ scope.row.username }}</span>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="botRating" label="Bot战力"/>
                <el-table-column prop="rating" label="天梯分"/>
            </el-table>
            <div style="display: flex">
                <div style="flex: 1"></div>
                <el-pagination
                        style="margin-top: 5px"
                        background
                        layout="prev, pager, next"
                        :total="totalUsers"
                        hide-on-single-page
                        :page-size="3"
                        @current-change="pull_page"
                />
            </div>
        </el-card>
    </div>
</template>


<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {get} from "@/util/request";
import NavaBar from "@/components/NavaBar.vue";

let users = ref([])
let totalUsers = ref(0)
const pull_page = (page: number) => {
    get({
        url: '/api/ranklist/getlist/',
        isAuth: true,
        data: {
            page: page
        },
        success: (data) => {
            users.value = data.users
            totalUsers.value = data.users_count
        }
    })
}
onMounted(() => {
    pull_page(1)
})

</script>


<style scoped>


.card-container {
    display: flex;
    justify-content: center;
}


.card-header {
    display: flex;
    justify-content: center;
    align-items: center;
}

.box-card {
    margin-top: 50px;
    width: 80%;
    text-align: center;
}

.gradient-text {
    font-size: 24px;
    font-weight: bold;
    background: linear-gradient(45deg, #78f3c2, #4888a4);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}
</style>