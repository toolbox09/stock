import { Auth } from '@/entities';
// import {} from ''



export const apis = {
  auth : {
    login : ( id : string, password : string ) : Promise<Auth> => {
      return new Promise(( resolve )=>{
        resolve({ id : id, keyword : 'A' })
      })
    }
  }
}