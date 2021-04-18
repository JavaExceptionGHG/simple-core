/*
 * Copyright  (c) 2021.  Jonathan Bull Contact at jonathan@jbull.dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.jbull.corev.api;

import dev.jbull.corev.api.inventory.InventoryManager;
import dev.jbull.corev.api.utils.NameUUID;
import dev.jbull.corev.inventory.anvil.AnvilInventory;
import lombok.Data;

public class CoreV {
    private static CoreV instance;
    private NameUUID nameUUID;
    private InventoryManager inventoryManager;

    public static CoreV getInstance(){
        if (instance == null) instance = new CoreV();
        return instance;
    }

    public CoreV(){
        nameUUID = new NameUUID();
        inventoryManager = new InventoryManager();
    }

    public NameUUID getNameUuidFetcher() {
        return nameUUID;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
