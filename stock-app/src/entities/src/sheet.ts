export type Sheet<T extends { id : string } > = Record<string,T>;

export class SheetOp {
  public static get<T extends { id : string }>(sheet : Sheet<T>, id : string) {
    return sheet[id];
  }

  public static has<T extends { id : string }>( sheet : Sheet<T>, id : string ) {
    return SheetOp.get(sheet, id) ? true : false;
  }

  public static list<T extends { id : string }>( sheet : Sheet<T>) {
    return Object.keys(sheet).map( key=> sheet[key] );
  }

  public static set<T extends { id : string }>( sheet : Sheet<T>, obj : T  ) {
    sheet[obj.id] = obj;
  }

  public static delete<T extends { id : string }>( sheet : Sheet<T>, id : string ) {
    if(sheet[id]) {
      delete sheet[id]; 
    }
  }
}