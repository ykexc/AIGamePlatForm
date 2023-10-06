<template>
    <el-card>
        <template #header>
            <div class="card-header">
                <span class="gradient-text">我的Bot</span>
                <AddBot
                    @re-fresh="getBots"
                />
            </div>
        </template>
        <el-table ref="table" v-loading="loading" height="187.5px" :data="bots" table-layout="fixed"
                  style="width: 100%">
            <template #empty>
                <span style="text-align: center; font-size: 20px; font-weight: bolder">快去创建你的bot吧!!!</span>
            </template>
            <el-table-column fixed prop="title" label="bot名称"/>
          <el-table-column fixed label="bot种类">
            <template #default="scope">
              {{scope.row.game === 'snake' ? '绕蛇' : '五子棋'}}
            </template>
          </el-table-column>
            <el-table-column prop="type" label="语言">
                <template #default="scope">
                    <el-tag effect="dark" :color="scope.row.type === 'java' ? '#1AE694' : '#DDDD22'">
                        <span style="font-weight: bold">{{ scope.row.type === 'java' ? scope.row.type : 'c++' }}</span>
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column prop="createTime" label="创建时间"/>
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button-group class="ml-3">
                        <UpdateBot
                                :bot-id="scope.row.id"
                                :title="scope.row.title"
                                :content="scope.row.content"
                                :description="scope.row.description"
                                :type="scope.row.type"
                                :game="scope.row.game"
                                @re-fresh="getBots"
                        />
                        <el-popconfirm
                                confirm-button-text="是"
                                confirm-button-type="danger"
                                cancel-button-text="否"
                                :icon="InfoFilled"
                                icon-color="#FF0000"
                                title="确认删除吗?"
                                @confirm="deleteBots(scope.row.id)"
                        >
                            <template #reference>
                                <el-button type="danger" :icon="Delete">删除</el-button>
                            </template>
                        </el-popconfirm>
                    </el-button-group>
                </template>
            </el-table-column>
        </el-table>
    </el-card>
</template>


<script setup lang="ts">

import type {Ref} from "vue";
import {onMounted, ref} from "vue";
import {get, post} from "@/util/request";
import {Delete, InfoFilled} from "@element-plus/icons-vue";
import {ElMessage} from "element-plus";
import UpdateBot from "@/components/bot/UpdateBot.vue";
import AddBot from "@/components/bot/AddBot.vue";

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

const bots: Ref<Array<Bot>> = ref(new Array<Bot>())
const loading: Ref<boolean> = ref(true)


onMounted((): void => {
    getBots()
})

const table: Ref = ref()

const handleCurrentChange = () => {
    table.value.$refs.scrollBarRef.setScrollTop(0)
}

const getBots = (): void => {
    get({
        url: '/api/user/bot/getlist/',
        isAuth: true,
        success: (data) => {
            bots.value = data
            loading.value = false
        }
    })
}

const deleteBots = (botId: number): void => {
    post(
        {
            url: '/api/user/bot/remove/',
            data: {
                bot_id: botId
            },
            isAuth: true,
            success: (data) => {
                if (data.error_message === 'success') {
                    ElMessage.success('删除成功')
                    loading.value = false
                    getBots()
                    handleCurrentChange()
                } else {
                    ElMessage.error(data.error_message)
                }
            }
        }
    )
}


</script>

<style scoped>
.gradient-text {
    flex: 1;
    font-size: 24px;
    font-weight: bold;
    background: linear-gradient(45deg, #78f3c2, #4888a4);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
}

.card-header {
    display: flex;
}
</style>