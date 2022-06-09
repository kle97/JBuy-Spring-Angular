import { createStore } from "@ngneat/elf";
import { Injectable } from "@angular/core";
import { addEntitiesFifo, deleteEntities, hasEntity, selectAllEntities, withEntities } from "@ngneat/elf-entities";
import { localStorageStrategy, persistState } from "@ngneat/elf-persist-state";
import { map } from "rxjs";

export interface SearchText {
  id: string,
  text: string,
  category: string,
}

export const recentlySearchedTextStore = createStore({ name: "recentlySearchedText" },
  withEntities<SearchText>());

@Injectable({
  providedIn: "root",
})
export class RecentlySearchedTextRepository {
  recentlySearchedTexts$ = recentlySearchedTextStore.pipe(selectAllEntities()).pipe(
    map(searhTexts => searhTexts.reverse()),
  );

  addSearchText(text: string, category: string = "All") {
    if (!recentlySearchedTextStore.query(hasEntity(text + "_" + category))) {
      recentlySearchedTextStore.update(addEntitiesFifo([{
        id: text + "_" + category,
        text,
        category,
      }], { limit: 15 }));
    } else {
      recentlySearchedTextStore.update(deleteEntities(text + "_" + category));
      recentlySearchedTextStore.update(addEntitiesFifo([{
        id: text + "_" + category,
        text,
        category,
      }], { limit: 10 }));
    }
  }

  removeSearchText(text: string, category: string = "All") {
    recentlySearchedTextStore.update(deleteEntities(text + "_" + category));
  }
}

export const recentlySearchedTextsPersist = persistState(recentlySearchedTextStore, {
  key: "recentlySearchedTexts",
  storage: localStorageStrategy,
});
