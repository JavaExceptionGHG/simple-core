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

package dev.jbull.simplecore;

import dev.jbull.simplecore.config.IConfig;
import dev.jbull.simplecore.database.mogodb.MongoDatabase;
import dev.jbull.simplecore.database.redis.Jedis;
import dev.jbull.simplecore.database.sql.MySQL;
import dev.jbull.simplecore.inventory.InventoryManager;
import dev.jbull.simplecore.player.nameUuid.NameUuidFetcher;
import dev.jbull.simplecore.utils.ExecuteScheduler;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Getter
public class Core {
    @Getter
    private static Core instance;
    private ExecuteScheduler scheduler;
    private MySQL mysql;
    private NameUuidFetcher nameUuidFetcher;
    private IConfig yamlConfig;
    private InventoryManager inventoryManager;
    private Jedis jedis;
    private final dev.jbull.simplecore.logger.Logger logger;
    private final boolean debug = false;

    public Core(IConfig yamlConfig, Logger logger){
        instance = this;
        this.logger = new dev.jbull.simplecore.logger.Logger(logger);
        scheduler = new ExecuteScheduler();
    }

    public void load(){
        this.yamlConfig = yamlConfig;
        yamlConfig.load(result -> {
            yamlConfig.addDefault("debug", false);
            yamlConfig.addDefault("bungeecord", false);
            yamlConfig.addDefault("database.mysql.host", "127.0.0.1");
            yamlConfig.addDefault("database.mysql.user", "root");
            yamlConfig.addDefault("database.mysql.password", "Test1234");
            yamlConfig.addDefault("database.mysql.database", "Core");
            yamlConfig.addDefault("database.mysql.port", "3306");
            yamlConfig.addDefault("database.redis.host", "127.0.0.1");
            yamlConfig.addDefault("database.redis.port", 6379);
            yamlConfig.addDefault("database.redis.password", "");
            yamlConfig.addDefault("database.redis.use", false);
            yamlConfig.setDefaults();
        });
        if (yamlConfig.getBoolean("database.redis.use")){
            jedis = new Jedis();
        }
        nameUuidFetcher = new NameUuidFetcher();
        inventoryManager = new InventoryManager();
        this.mysql = new MySQL(yamlConfig.getString("database.mysql.host"), yamlConfig.getString("database.mysql.user")
                , yamlConfig.getString("database.mysql.password"), yamlConfig.getString("database.mysql.database")
                , yamlConfig.getString("database.mysql.port"));
        mysql.update("CREATE TABLE IF NOT EXISTS nameuuid(UUID VARCHAR(64), NAME TEXT)");
        mysql.update("CREATE TABLE IF NOT EXISTS language(UUID VARCHAR(64), LANGUAGE TEXT)");
        MongoDatabase mongoDatabase = new  MongoDatabase();
        mongoDatabase.connect();
        UUID uuid = UUID.randomUUID();
        mongoDatabase.insertOne("L", new Document().append("UUID", uuid.toString()));
        System.out.println(mongoDatabase.getDocument("L", new Document().append("UUID", uuid.toString())).getString("UUID"));

    }

}
