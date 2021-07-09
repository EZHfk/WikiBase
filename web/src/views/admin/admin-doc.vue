<template>
  <a-layout>
    <a-layout-content :style="{ background:'#fff', padding: '0 24px', margin: '24px', minHeight: '280px' }">
      <p>
<!--        <a-button type="primary" @click="add()" size="large">-->
<!--          新增-->
<!--        </a-button>-->
        <a-form layout="inline" :model="param">
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="handleQuery()">
              查询
            </a-button>
          </a-form-item>
          <a-form-item>
            <a-button type="primary" @click="add()" size="large">
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
              :columns="columns"
              :row-key="record => record.id"
              :data-source="level1"
              :loading="loading"
              :pagination="false"
              :defaultExpandAllRows="true"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
<!--        <template v-slot:doc="{ text, record }">-->
<!--          <span>{{ getDocName(record.doc1Id) }} / {{ getDocName(record.doc2Id) }}</span>-->
<!--        </template>-->
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <router-link :to="'/admin/doc?docId=' + record.id">
              <a-button type="primary">
                文档管理
              </a-button>
            </router-link>
            <a-button type="primary" @click="edit(record)">
              编辑
            </a-button>
            <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="是"
                    cancel-text="否"
                    @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>
          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
          title="文档表单"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >
    <a-form :model="doc" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="父文档">
        <a-input v-model:value="doc.parent" />
        <a-select
                v-model:value="doc.parent"
                ref="select"
        >
          <a-select-option :value="0">
            无
          </a-select-option>
          <a-select-option v-for="c in level1" :key="c.id" :value="c.id" :disabled="doc.id === c.id">
            {{c.name}}
          </a-select-option>
        </a-select>
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>

</template>

<script lang="ts">
  import { defineComponent, onMounted, ref } from 'vue';
  import axios from 'axios';
  import { message } from 'ant-design-vue';
  import {Tool} from "@/util/tool";

  export default defineComponent({
    name: 'AdminDoc',
    setup() {
      const param = ref();
      param.value = {};
      const docs = ref();
      const loading = ref(false);

      const columns = [
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '父文档',
          key:'parent',
          dataIndex:'parent'
        },
        {
          title: '顺序',
          dataIndex: 'sort'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      /**
       * 一级文档树，children属性就是二级文档
       * [{
       *   id: "",
       *   name: "",
       *   children: [{
       *     id: "",
       *     name: "",
       *   }]
       * }]
       */
      const level1 = ref(); // 一级文档树，children属性就是二级文档

      /**
       * 数据查询
       **/
      const handleQuery = () => {
        loading.value = true;
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        level1.value=[];
        axios.get("http://127.0.0.1:8880/doc/all",).then((response)=>{
          loading.value = false;
          const data = response.data;
          if(data.success){
            docs.value = data.content;
            level1.value=Tool.array2Tree(docs.value,0);
            console.log("原始数组：",doc.value);
            console.log("树形结构：",level1);
          }
          else{
            message.error(data.message);
          }
        });
      };


      const doc = ref({});
      const modalVisible = ref(false);
      const modalLoading = ref(false);
      const handleModalOk = () => {
        modalLoading.value=true;
        axios.post("http://127.0.0.1:8880/doc/save",doc.value).then((response)=>{
          modalLoading.value=false;
          const data = response.data; //data = CommonResp
          if(data.success){
            modalVisible.value=false;

            // Restart List
            handleQuery();
          }
          else{
            message.error(data.message);
          }
        });
      };


      /**
       * 编辑
       */
      const edit = (record: any) => {
        modalVisible.value = true;
        doc.value=Tool.copy(record);
        // docIds.value = [doc.value.doc1Id, doc.value.doc2Id]
      };

      /**
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        doc.value = {};
      };

      const handleDelete = (id: number) => {
        axios.delete("http://127.0.0.1:8880/doc/delete/" + id).then((response) => {
          const data = response.data; // data = commonResp
          if (data.success) {
            // 重新加载列表
            handleQuery();
          }
        });
      };

      // const level1 =  ref();
      // let docs: any;
      // /**
      //  * 查询所有文档
      //  **/
      // const handleQueryDoc = () => {
      //   loading.value = true;
      //   axios.get("/doc/all").then((response) => {
      //     loading.value = false;
      //     const data = response.data;
      //     if (data.success) {
      //       docs = data.content;
      //       console.log("原始数组：", docs);
      //
      //       level1.value = [];
      //       level1.value = Tool.array2Tree(docs, 0);
      //       console.log("树形结构：", level1.value);
      //
      //       // 加载完文档后，再加载文档，否则如果文档树加载很慢，则文档渲染会报错
      //       handleQuery({
      //         page: 1,
      //         size: pagination.value.pageSize,
      //       });
      //     } else {
      //       message.error(data.message);
      //     }
      //   });
      // };
      //
      // const getDocName = (cid: number) => {
      //   // console.log(cid)
      //   let result = "";
      //   docs.forEach((item: any) => {
      //     if (item.id === cid) {
      //       // return item.name; // 注意，这里直接return不起作用
      //       result = item.name;
      //     }
      //   });
      //   return result;
      // };

      onMounted(() => {
        handleQuery();

      });

      return {
        param,
        docs,

        columns,
        loading,
        handleQuery,
        // getDocName,
        //
        edit,
        add,
        //
        doc,
        modalVisible,
        modalLoading,
        handleModalOk,
        // docIds,
        level1,
        //
        handleDelete
      }
    }
  });
</script>
