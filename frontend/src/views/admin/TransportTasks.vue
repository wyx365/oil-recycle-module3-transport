<!-- 模块3：无人机转运与接口监控。 -->
<template>
  <div class="page">
    <PageHeader title="无人机转运任务记录"/>
    <el-card>
      <el-table :data="rows">
        <el-table-column prop="taskNo" label="任务编号"/>
        <el-table-column prop="stationId" label="站点ID"/>
        <el-table-column label="废油类型">
          <template #default="s">{{ enumLabel(s.row.oilType) }}</template>
        </el-table-column>
        <el-table-column prop="weight" label="重量(kg)"/>
        <el-table-column prop="droneNo" label="无人机编号"/>
        <el-table-column label="状态">
          <template #default="s">
            <StatusTag :value="s.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="220">
          <template #default="s">
            <el-button size="small" @click="push(s.row.id)">推送</el-button>
            <el-button size="small" type="success" @click="mockFinish(s.row)">模拟完成</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>
<script setup lang="ts">
import {onMounted, ref} from 'vue'
import PageHeader from '@/components/PageHeader.vue'
import StatusTag from '@/components/StatusTag.vue'
import {transportApi} from '@/api'
import {ElMessage} from 'element-plus'
import {enumLabel} from '@/utils/dict'

// 转运任务列表。
const rows = ref<any[]>([])
// 加载转运任务记录。
const load = async () => {
  const data: any = await transportApi.page({size: 50});
  rows.value = data.records || []
}
// 模拟推送任务到无人机平台。
const push = async (id: number) => {
  await transportApi.push(id);
  ElMessage.success('推送成功');
  load()
}
// 模拟对方无人机平台回调完成状态。
const mockFinish = async (row: any) => {
  await transportApi.callback({
    taskNo: row.taskNo,
    droneNo: 'DR-DEMO-01',
    status: 'FINISHED',
    currentLocation: row.endPoint,
    battery: 78,
    message: '模拟无人机已完成转运'
  })
  ElMessage.success('模拟回调完成')
  load()
}
onMounted(load)
</script>
