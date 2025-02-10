import { Sheet } from '../internal';

export interface Barcode {
  id : string;
  barcode : string;
  count : number;
  matched : boolean;
  created : string;
}

export type BarcodeSheet = Sheet<Barcode>;


function randomArray<T>(array: T[]): T {
  if (!Array.isArray(array) || array.length === 0) {
    throw new Error("The input must be a non-empty array.");
  }
  
  const randomIndex = Math.floor(Math.random() * array.length);
  return array[randomIndex];
}

export class BarcodeOp {

  static testCodes = 
  [
    "XX06194731018",
    "8806194731018",
    "8806194731025",
    "8806194731049",
    "8806194731070",
    "8806188365892",
    "XX06188365861",
    "XX06188365854",
    "8806188365885",
    "8806188365878",
    "XX06188365960",
    "8806188365977",
  ]

  public static randomBarcode() {
    return randomArray( BarcodeOp.testCodes);
  }
}
