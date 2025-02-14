import { Auth, FileInfo, ProjectInfo, ProjectState, Master, Work  } from '@/entities';
import { axios } from './axios';
import { CreateProjectRequest, UpdateWorkRequest } from './io/project';


export const apis = {
  auth : {
    login : ( id : string, password : string ) : Promise<Auth> => {
      return new Promise(( resolve )=>{
        resolve({ id : id, keyword : 'A' })
      })
    }
  },
  master : {
    fileList : async () => {
      return await axios<FileInfo[]>('master/file-list', {  
        method : 'GET'
      })
    },
    matchFileList : async () => {
      return await axios<FileInfo[]>('master/match-file-list', {  
        method : 'GET'
      })
    },
    get : async ( fileName : string ) => {
      return await axios<Master>('master', {  
        method : 'GET',
        params : {
          fileName : fileName,
        }
      })
    }
  },
  project : {
    create : async ( req : CreateProjectRequest ) => {
      return await axios<boolean>('project/create', {  
        method : 'POST',
        body : {
          name : req.name,
          masterUrl : req.masterUrl,
          matchUrl : req.matchUrl,
        }
      })
    },
    infoList : async () => {
      return await axios<ProjectInfo[]>('project/info-list', {  
        method : 'GET'
      })
    },
    stateList : async () => {
      return await axios<ProjectState[]>('project/state-list', {  
        method : 'GET'
      })
    },
    state : async ( projectName : string ) => {
      return await axios<ProjectState>('project/state',{
        method : 'GET',
        params : {
          projectName : projectName,
        }
      })
    }
  },
  work : {
    get : async (projectName : string, masterUrl? : string ) => {
      return await axios<Work>('work',{
        method : 'GET',
        params : {
          projectName : projectName,
          masterUrl : masterUrl as any,
        }
      })
    },
    update :  async ( req : UpdateWorkRequest ) => {
      return await axios<Work>('work/update',{
        method : 'POST',
        body : req,
      })
    },
    collect : async ( projectName : string ) => {
      return await axios<boolean>('work/collect', {
        method : 'POST',
        params : {
          projectName : projectName,
        }
      })
    }
  }
}