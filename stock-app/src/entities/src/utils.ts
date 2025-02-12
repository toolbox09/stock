import { randomUUID as uuid } from 'expo-crypto';
import { Work, ProjectInfo, Section, SectionSheet, SheetOp, Barcode } from '../internal';

export class Utils {


  public static createSection( name : string ) : Section {
    return {
      id : uuid(),
      name : name,
      created : new Date().toLocaleTimeString(),
      barcodes : {},
    }
  }

  public static createBarcode( barcode : string, count : number, matched : boolean ) : Barcode {
    return {
      id : uuid(),
      barcode : barcode,
      count : count,
      matched : matched,
      created :  new Date().toLocaleString(),
    }
  }

  public static extractNumber( str : string, keyword : string) {
    str = str.trim();
    try{
      str = str.replace(keyword, '');
      const result = Number(str);
      return isNaN(result) ? -1 :  result;
    }catch {
      return -1;
    }
  }

  public static maxNumber( nums : number[] ) {
    let max = 0;
    nums.forEach( n => { max = Math.max( n, max)  } );
    return max;
  }

  public static makeSectionName(keyword : string, sections : SectionSheet) {
    const nums = SheetOp.list(sections).map( s => Utils.extractNumber( s.name, keyword ) );
    const max = Utils.maxNumber(nums);
    return (max+1).toString().padStart(3,"0") + keyword;
  }

}