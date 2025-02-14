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
    "SC0MF22X021255",
    "SC0MF22X021265",
    "SC0MF22X021270",
    "SC0MF22X021275",
    "SC0MF22X021280",
    "SC0MF22X022270",
    "SC0MF22X042270",
    "SC0MF22X051260",
    "SC0MF22X051265",
    "SC0MF22X051270",
    "SC0MF22X051280",
    "SC0MF22X052260",
    "SC0MF22X052265",
    "SC0MF22X052270",
    "SC0MF22X071250",
    "SC0MF22X071260",
    "SC0MF22X071265",
    "SC0MF22X071275",
    "SC0MF22X072260",
    "SC0MF22X072265",
    "SC0MF22X072280",
    "SC0MF22X080250",
    "SC0MF22X080260",
    "SC0MF22X080265",
    "SC0MF22X080270",
    "SC0MF22X080280",
    "SC0MF22X081255",
    "SC0MF22X08125X",
    "SC0MF22X08125A",
    "SC0MF22X08125B",
  ]

  public static randomBarcode() {
    return randomArray( BarcodeOp.testCodes);
  }
}
